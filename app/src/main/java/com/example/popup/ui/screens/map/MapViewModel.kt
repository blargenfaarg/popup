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
import kotlinx.coroutines.launch
import javax.inject.Inject
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
        if (previousMapBounds == null) {
            previousMapBounds = bounds
            getPostMapDataForBound(bounds)
            return
        }

        previousMapBounds = bounds
        getPostMapDataForBound(bounds)
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
                // Merge the two sets of markers
                val updatedMarkers = result.data.toSet()
                updatedMarkers.plus(_postMarkers.value)

                _postMarkers.value = updatedMarkers
            }
        }
    }

    /**
     * Listen for location updates
     */
    override fun locationUpdated(location: AndroidLocation) {
        _userLocation.value = LatLng(location.latitude, location.longitude)
    }
}