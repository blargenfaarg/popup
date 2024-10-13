package com.example.popup.di.location

import android.location.Location

/**
 * A functional interface for listening to location updates
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 10/11/2024
 */
interface ILocationUpdatedListener {

    /**
     * Get alerted that the user's location has been updated
     * @param location the updated location
     */
    fun locationUpdated(location: Location)
}