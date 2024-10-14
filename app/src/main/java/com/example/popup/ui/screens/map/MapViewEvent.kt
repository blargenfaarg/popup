package com.example.popup.ui.screens.map

import com.example.popup.ui.util.ViewModelEvent
import com.google.android.gms.maps.model.LatLngBounds

/**
 * The events for the map view screen
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 10/11/2024
 */
sealed class MapViewEvent: ViewModelEvent {
    data class PostClicked(val id: Long): MapViewEvent()
    data class MapBoundsChanged(val bounds: LatLngBounds): MapViewEvent()
    data object PopupSheetDismissed: MapViewEvent()
}