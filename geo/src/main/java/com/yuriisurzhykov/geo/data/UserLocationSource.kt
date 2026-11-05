package com.yuriisurzhykov.geo.data

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.yuriisurzhykov.geo.LatLng
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface UserLocationSource {

    @kotlin.jvm.Throws(LocationUnavailableException::class)
    suspend fun location(): LatLng

    /**
     * The last known location is good enough for coarse needs like finding a city and is much
     * faster than waiting for a fresh one. The location permission is checked by the caller.
     */
    abstract class Abstract(
        private val locationClient: FusedLocationProviderClient
    ) : UserLocationSource {

        @SuppressLint("MissingPermission")
        override suspend fun location(): LatLng {
            try {
                val location = locationClient.lastLocation.await()
                    ?: locationClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY, null
                    ).await()
                    ?: throw LocationUnavailableException("there is no known location")
                return LatLng(location.latitude, location.longitude)
            } catch (e: CancellationException) {
                throw e
            } catch (e: LocationUnavailableException) {
                throw e
            } catch (e: Exception) {
                throw LocationUnavailableException(e.toString(), e)
            }
        }
    }

    class Base @Inject constructor(
        locationClient: FusedLocationProviderClient
    ) : Abstract(locationClient)
}
