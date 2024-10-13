package com.example.popup.ui.screens.map

import com.example.popup.ui.util.ViewModelEvent
import com.google.android.gms.maps.model.LatLngBounds

sealed class MapViewEvent: ViewModelEvent {
    data class PostClicked(val id: Long): MapViewEvent()
    data class MapBoundsChanged(val bounds: LatLngBounds): MapViewEvent()
}