package com.example.popup.ui.screens.map

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.popup.R
import com.example.popup.di.location.LocationHandler
import com.example.popup.mock.MockApiService
import com.example.popup.model.domain.common.PostType
import com.example.popup.ui.reusable.SwipeImageCollection
import com.example.popup.ui.theme.BluePrimary
import com.example.popup.ui.theme.GrayOutlineSecondary
import com.example.popup.ui.util.getMapIcon
import com.example.popup.ui.util.getPostIconResourceId
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
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            it != SheetValue.PartiallyExpanded
        }
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

    // Track the camera state position
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position }
            .collect { _ ->
                if (!cameraPositionState.isMoving) {
                    coroutineScope.launch {
                        delay(1500)
                        val bounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
                        bounds?.let {
                            viewModel.onEvent(MapViewEvent.MapBoundsChanged(it))
                        }
                    }
                }
            }
    }

    if (showPopup) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                viewModel.onEvent(MapViewEvent.PopupSheetDismissed)
            }
        ) {
            PostPopUp(viewModel = viewModel)
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
                    },
                    icon = getMapIcon(marker.postType)
                )
            }
        }

        MapLegend()
    }
}

@Composable
fun MapLegend() {
    var isExpanded by remember { mutableStateOf(false) }
    val transition = updateTransition(isExpanded, label = "FAB transition")

    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) },
        label = "FAB scale"
    ) { expanded ->
        if (expanded) 1f else 0f
    }

    val fabScale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) },
        label = "FAB visibility scale"
    ) { expanded ->
        if (expanded) 0f else 1f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .scale(scale)
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White)
                .border(1.dp, GrayOutlineSecondary, RoundedCornerShape(25.dp))
                .padding(16.dp)
                .width(175.dp)
        ) {
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Legend",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(75.dp))
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { isExpanded = false }
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(5.dp))
                repeat(PostType.entries.size) { index ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val postType = PostType.entries[index]
                        Image(
                            painter = painterResource(id = getPostIconResourceId(postType)),
                            contentDescription = "Post Type Icon",
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = postType.prettyName,
                            fontSize = 14.sp
                        )
                    }
                    if (index != PostType.entries.size - 1) {
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                isExpanded = true
            },
            containerColor = Color.White,
            modifier = Modifier
                .scale(fabScale)
                .align(Alignment.TopStart)
        ) {
            Image(
                painter = painterResource(id = R.drawable.map),
                contentDescription = "Map Legend",
                modifier = Modifier
                    .size(35.dp)
            )
        }
    }
}

@Composable
fun PostPopUp(
    viewModel: MapViewModel
) {
    val post by viewModel.postToShow.collectAsState()
    val errorLoadingPost by viewModel.errorLoadingPost.collectAsState()

    if (errorLoadingPost) {
        Text(
            text = "Unable to load posting. Please try again later.",
            textAlign = TextAlign.Center
        )
    } else if (post == null ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = BluePrimary)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = post!!.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Event Type: ",
                    fontSize = 16.sp
                )
                Text(
                    text = post!!.type.prettyName,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            SwipeImageCollection(
                images = post!!.pictures,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            post!!.description?.let {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Description:",
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.Underline
                    )
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                    )
                }
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