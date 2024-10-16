package com.example.popup.ui.util

import com.example.popup.R
import com.example.popup.model.domain.common.PostType
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

/**
 * A file with helper functions for dealing with post icons
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */

/**
 * Get the appropriate map icon to show depending on the post type
 */
fun getMapIcon(postType: PostType): BitmapDescriptor {
    return BitmapDescriptorFactory.fromResource(getPostIconResourceId(postType))
}

/**
 * Get the right resource id for the drawable that corresponds to the passed in post type
 */
fun getPostIconResourceId(postType: PostType): Int {
    return when(postType) {
        PostType.YARD_SALE -> R.drawable.yard_sale
        PostType.GARAGE_SALE -> R.drawable.garage_sale
        PostType.FARMERS_MARKET -> R.drawable.farmers_market
        PostType.FOOD_TRUCK -> R.drawable.food_truck
        PostType.MUSIC_EVENT -> R.drawable.music_event
        PostType.SEASONAL -> R.drawable.seasonal_event
        PostType.SPORTING -> R.drawable.sporting_events
        PostType.OTHER -> R.drawable.other
    }
}