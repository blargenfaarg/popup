package com.example.popup.model.domain.common

/**
 * Represents the backend model for a PostType
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
enum class PostType(val prettyName: String) {
    YARD_SALE("Yard Sale"),
    GARAGE_SALE("Garage Sale"),
    FARMERS_MARKET("Farmers Market"),
    FOOD_TRUCK("Food Truck"),
    MUSIC_EVENT("Music Event"),
    SEASONAL("Seasonal"),
    SPORTING("Sporting Events"),
    OTHER("Other");
}