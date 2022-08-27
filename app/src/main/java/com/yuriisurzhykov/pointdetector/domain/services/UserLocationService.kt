package com.yuriisurzhykov.pointdetector.domain.services

import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import javax.inject.Inject

class UserLocationService @Inject constructor(
//    private val mapper: Mapper<com.google.android.gms.maps.model.LatLng, LatLng>
) : IUserLocationService {

    override fun currentUserLocation(): LatLng {
        val location = LocationManager.getCurrentLocation()
        return location?.let { loc ->
            LatLng(loc.latitude, loc.longitude)
        } ?: LatLng(0.0, 0.0)
    }
}