package com.yuriisurzhykov.pointdetector.data.remote

import com.google.maps.android.SphericalUtil
import com.google.maps.android.ktx.utils.sphericalDistance
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.LatLng

class LocalDistanceCalculateService(private val mapper: Mapper<LatLng, com.google.android.gms.maps.model.LatLng>) :
    DistanceCalculateService {

    override fun calculateDistance(coordinates1: LatLng, coordinates2: LatLng): Double {
        return SphericalUtil.computeDistanceBetween(mapper.map(coordinates1), mapper.map(coordinates2)) / 1000
    }
}