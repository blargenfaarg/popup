package com.example.popup.di.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

/**
 * A bean that handles the users location
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 10/11/2024
 */
class LocationHandler {

    /**
     * Provided from the main activity context
     */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var appContext: Context

    /**
     * The last known location of the user
     */
    private var lastLocation: Location? = null

    /**
     * Listeners to be notified of the location change
     */
    private val listeners: MutableList<ILocationUpdatedListener> = mutableListOf()
    private val lock = Any()

    /**
     * Add a listener to be notified of when the user's location is updated, returning to this new
     * listener the currently know location
     */
    fun addListener(listener: ILocationUpdatedListener) {
        synchronized(lock) {
            listeners.add(listener)
            lastLocation?.let {
                listener.locationUpdated(it)
            }
        }
    }

    /**
     * Remove a listener that no longer wishes to hear of location updates
     */
    fun removeListener(listener: ILocationUpdatedListener) {
        synchronized(lock) {
            listeners.remove(listener)
        }
    }

    /**
     * Set the location provider and the application context
     */
    fun setup(client: FusedLocationProviderClient, context: Context) {
        fusedLocationProviderClient = client
        appContext = context

        tryMonitorLocation()
    }

    /**
     * Tries to add a success listener to the location provider client to be updated of the
     * new location
     */
    private fun tryMonitorLocation() {
        // Checks location permissions
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO("Maybe ask for permission again?")
            return
        }

        // Add a listener to get the location
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                updateLocation(location)
            }
        }
    }

    /**
     * Update the last know location and let any listeners know of the update
     */
    private fun updateLocation(location: Location) {
        lastLocation = location

        listeners.forEach { listener ->
            listener.locationUpdated(location)
        }
    }
}