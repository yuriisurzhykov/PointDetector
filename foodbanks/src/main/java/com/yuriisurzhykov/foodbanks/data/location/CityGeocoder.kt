package com.yuriisurzhykov.foodbanks.data.location

import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.yuriisurzhykov.foodbanks.data.point.LatLng
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume

interface CityGeocoder {

    /**
     * Name of the city (locality) at the [location], or an empty string
     * when it is not known, for example when there is no network.
     */
    suspend fun locality(location: LatLng): String

    abstract class Abstract(
        private val geocoder: Geocoder
    ) : CityGeocoder {

        override suspend fun locality(location: LatLng): String {
            if (!Geocoder.isPresent()) return ""
            return addresses(location).firstNotNullOfOrNull { it.locality ?: it.subAdminArea }
                ?: ""
        }

        private suspend fun addresses(location: LatLng): List<Address> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(location.lat, location.lng, MAX_RESULTS,
                        object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) =
                                continuation.resume(addresses)

                            override fun onError(errorMessage: String?) =
                                continuation.resume(emptyList())
                        })
                }
            } else {
                withContext(kotlinx.coroutines.Dispatchers.IO) {
                    try {
                        @Suppress("DEPRECATION")
                        geocoder.getFromLocation(location.lat, location.lng, MAX_RESULTS)
                            ?: emptyList()
                    } catch (e: IOException) {
                        emptyList()
                    }
                }
            }
        }

        private companion object {
            const val MAX_RESULTS = 3
        }
    }

    class Base @Inject constructor(geocoder: Geocoder) : Abstract(geocoder)
}
