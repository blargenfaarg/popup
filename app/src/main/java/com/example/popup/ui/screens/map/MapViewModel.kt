package com.example.popup.ui.screens.map

import androidx.lifecycle.viewModelScope
import com.example.popup.di.location.ILocationUpdatedListener
import com.example.popup.di.location.LocationHandler
import com.example.popup.model.domain.Post
import com.example.popup.model.domain.common.Location
import com.example.popup.model.request.post.GetMapDataRequest
import com.example.popup.model.response.PostMapData
import com.example.popup.networking.api.IApiService
import com.example.popup.ui.util.APopUpViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import android.location.Location as AndroidLocation

/**
 * The map screen view model
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 10/11/2024
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    private val apiService: IApiService,
    private val locationHandler: LocationHandler
): APopUpViewModel<MapViewEvent>(), ILocationUpdatedListener {

    /**
     * Flow that stores the post map data loaded
     */
    private val _postMarkers = MutableStateFlow<Set<PostMapData>>(emptySet())
    val postMarkers: StateFlow<Set<PostMapData>> = _postMarkers.asStateFlow()

    /**
     * Track the user's location
     */
    private val _userLocation = MutableStateFlow(LatLng(0.0, 0.0))
    val userLocation: StateFlow<LatLng> = _userLocation.asStateFlow()

    /**
     * Track if we should show the bottom sheet pop-up
     */
    private val _showPopupDetail = MutableStateFlow(false)
    val showPopupDetail = _showPopupDetail.asStateFlow()

    /**
     * The post to show the details for
     */
    private val _postToShow = MutableStateFlow<Post?>(null)
    var postToShow = _postToShow.asStateFlow()
    private val _errorLoadingPost = MutableStateFlow(false)
    var errorLoadingPost = _errorLoadingPost.asStateFlow()

    private var previousMapBounds: LatLngBounds? = null

    /**
     * Default constructor
     */
    init {
        setupLocationListener()
    }

    /**
     * Add this view model as a listener for location updates, and set the user location to the
     * last know location
     */
    private fun setupLocationListener() {
        locationHandler.addListener(this)
    }

    /**
     * Catch when the view model is cleared and remove it as a listener from the location handler
     */
    override fun onCleared() {
        super.onCleared()

        locationHandler.removeListener(this)
    }

    /**
     * Handle map view events
     */
    override fun onEvent(event: MapViewEvent) {
        when (event) {
            is MapViewEvent.MapBoundsChanged -> handleCameraBoundsChanged(event.bounds)
            is MapViewEvent.PostClicked -> postMarkerClicked(event.id)
            is MapViewEvent.PopupSheetDismissed -> {
                _showPopupDetail.value = false
            }
        }
    }

    /**
     * Handle when a post map marker has been clicked
     */
    private fun postMarkerClicked(id: Long) {
        _errorLoadingPost.value = false
        _postToShow.value = null
        _showPopupDetail.value = true

        viewModelScope.launch {
            val response = apiService.getPost(id)

            if (response.wasSuccessful()) {
                _postToShow.value = response.data!!
                _showPopupDetail.value = true
            } else {
                _errorLoadingPost.value = true
            }
        }
    }

    /**
     * Checks if the bounds have changed significantly or not
     */
    private fun handleCameraBoundsChanged(bounds: LatLngBounds) {
        val shouldQuery = previousMapBounds?.let {
            haveBoundsSignificantlyChanged(it, bounds)
        } ?: true

        if (shouldQuery) {
            getPostMapDataForBound(bounds)
            previousMapBounds = bounds
        }
    }

    /**
     * Get post map data for the new bounds
     */
    private fun getPostMapDataForBound(bounds: LatLngBounds) {
        viewModelScope.launch {
            val northeast = Location(bounds.northeast.latitude, bounds.northeast.longitude)
            val southwest = Location(bounds.southwest.latitude, bounds.southwest.longitude)
            val northwest = Location(bounds.northeast.latitude, bounds.southwest.longitude)
            val southeast = Location(bounds.southwest.latitude, bounds.northeast.longitude)

            val request = GetMapDataRequest(
                topLeft = northwest,
                topRight = northeast,
                bottomRight = southeast,
                bottomLeft = southwest
            )

            val result = apiService.getPostMapData(request)
            if (result.wasSuccessful() && result.data != null) {
                _postMarkers.update { currentMarkers ->
                    currentMarkers + result.data.toSet()
                }
            }
        }
    }

    /**
     * Listen for location updates
     */
    override fun locationUpdated(location: AndroidLocation) {
        _userLocation.value = LatLng(location.latitude, location.longitude)
    }

    /**
     * Check if two lat lng bounds are significantly different (in this case, their area difference
     * ration is less than the threshold). Done to avoid spamming the api service as we poll
     * the google map camera position
     */
    private fun haveBoundsSignificantlyChanged(
        bounds1: LatLngBounds,
        bounds2: LatLngBounds,
        threshold: Double = 0.25
    ): Boolean {
        // get the northeast intersection, this should be the smaller of the two
        val intersectionNE = LatLng(
            min(bounds1.northeast.latitude, bounds2.northeast.latitude),
            min(bounds1.northeast.longitude, bounds2.northeast.longitude)
        )
        // get the southwest intersection, this should be the max of the two
        val intersectionSW = LatLng(
            max(bounds1.southwest.latitude, bounds2.southwest.latitude),
            max(bounds1.southwest.longitude, bounds2.southwest.longitude)
        )

        // handle the case where the bounds are completely different, e.g. the points have no
        // overlapping area
        if (intersectionNE.latitude < intersectionSW.latitude
            || intersectionNE.longitude < intersectionSW.longitude) {
            return true
        }

        // inner function to calculate the area
        fun area(bounds: LatLngBounds): Double {
            val width = abs(bounds.northeast.longitude - bounds.southwest.longitude)
            val height = abs(bounds.northeast.latitude - bounds.southwest.latitude)
            return width * height
        }

        // Get the areas (compare to the smaller of the two areas for better accuracy
        val intersectionArea = area(LatLngBounds(intersectionSW, intersectionNE))
        val smallerArea = min(area(bounds1), area(bounds2))

        // Return true if significantly different
        return  ((intersectionArea / smallerArea) < (1 - threshold))
    }
}