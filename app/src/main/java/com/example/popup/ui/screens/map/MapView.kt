package com.example.popup.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.popup.di.location.LocationHandler
import com.example.popup.mock.MockApiService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * The map screen view
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 10/11/2024
 */
@Composable
fun MapView(
    viewModel: MapViewModel = hiltViewModel()
) {
    val userLocation by viewModel.userLocation.collectAsState()
    val markers by viewModel.postMarkers.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    LaunchedEffect(userLocation) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(userLocation, 15f),
            durationMs = 1000
        )
    }

    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position }
            .collect { position ->
                println("Camera state updated")
                coroutineScope.launch {
                    delay(1000)
                    val bounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
                    bounds?.let {
                        viewModel.onEvent(MapViewEvent.MapBoundsChanged(it))
                    }
                }
            }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(
                isMyLocationEnabled = true
            ),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                val initialPosition = cameraPositionState.position.target
                val currentBounds = LatLngBounds.Builder()
                    .include(initialPosition)
                    .build()

                viewModel.onEvent(MapViewEvent.MapBoundsChanged(currentBounds))
            }
        ) {
            markers.forEach { marker ->
                Marker(
                    state = MarkerState(
                        position = LatLng(marker.location.latitude, marker.location.longitude)
                    ),
                    onClick = {
                        viewModel.onEvent(
                            event = MapViewEvent.PostClicked(
                                id = marker.id
                            )
                        )
                        true
                    }
                    // TODO("Set custom marker depending on marker.postType")
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MapViewPreview(

) {
    MapView(
        viewModel = MapViewModel(
            apiService = MockApiService(),
            locationHandler = LocationHandler()
        )
    )
}