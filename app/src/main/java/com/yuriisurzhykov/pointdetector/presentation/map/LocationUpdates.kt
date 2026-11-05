package com.yuriisurzhykov.pointdetector.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.concurrent.Executors

private const val MIN_INTERVAL_MILLIS = 5L
private const val MAX_DELAY_MILLIS = 3000L

/**
 * The position of the phone while the screen is in front: the last known one at once and then the
 * updates. It does nothing without the permission; the person can go on without the location.
 */
class LocationUpdates(
    private val context: Context,
    private val onLocation: (Location) -> Unit,
) {

    private val client by lazy { LocationServices.getFusedLocationProviderClient(context) }
    private val executor by lazy { Executors.newSingleThreadExecutor() }
    private val request by lazy {
        LocationRequest.Builder(MIN_INTERVAL_MILLIS)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(0)
            .setMaxUpdateDelayMillis(MAX_DELAY_MILLIS)
            .build()
    }
    private val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let(onLocation)
        }
    }

    val isAllowed: Boolean
        get() = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    fun start() {
        if (!isAllowed) return
        client.requestLocationUpdates(request, executor, callback)
        client.lastLocation.addOnSuccessListener { location -> location?.let(onLocation) }
    }

    fun stop() {
        client.removeLocationUpdates(callback)
    }
}
