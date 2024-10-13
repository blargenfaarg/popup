package com.example.popup.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.popup.di.location.LocationHandler
import com.example.popup.mock.MockApiService
import com.example.popup.model.domain.Post
import com.example.popup.model.domain.User
import com.example.popup.model.domain.common.Location
import com.example.popup.model.domain.common.PostType
import com.example.popup.model.domain.common.UTCTime
import com.example.popup.ui.reusable.SwipeImageCollection
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    viewModel: MapViewModel = hiltViewModel()
) {
    val userLocation by viewModel.userLocation.collectAsState()
    val markers by viewModel.postMarkers.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val showPopup by viewModel.showPopupDetail.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    // Move the map if the user's location updates
    LaunchedEffect(userLocation) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(userLocation, 15f),
            durationMs = 1000
        )
    }
    // Manage the popup detail view
    LaunchedEffect(showPopup) {
        if (showPopup) {
            scaffoldState.bottomSheetState.expand()
        } else {
            scaffoldState.bottomSheetState.hide()
        }
    }
    // Track the camera state position
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position }
            .collect { _ ->
                coroutineScope.launch {
                    delay(1000)
                    val bounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
                    bounds?.let {
                        viewModel.onEvent(MapViewEvent.MapBoundsChanged(it))
                    }
                }
            }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            PostPopUp(post = viewModel.postToShow)
        }
    ) {
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
}

@Composable
fun PostPopUp(
    post: Post?
) {
    post?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = it.title,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            SwipeImageCollection(
                images = it.pictures,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            it.description?.let {
                Text(
                    text = it,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PostPopupPreview(

) {
    PostPopUp(post = Post(
        id = 5,
        title = "Grandmas Yard Sale",
        description = "Selling all of my old clothes! Still in great shape.",
        location = Location(34.12567, longitude = -119.78546),
        type = PostType.YARD_SALE,
        postTime = UTCTime.now(),
        startTime = UTCTime.now(),
        endTime = UTCTime.now(),
        owner = User(
            id = 1,
            firstname = "Carol",
            lastname = "Wilkins",
            username = "Cookie Grandma",
            email = "granny@gmail.com",
        )
    ))
}

//@Preview(showBackground = true)
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