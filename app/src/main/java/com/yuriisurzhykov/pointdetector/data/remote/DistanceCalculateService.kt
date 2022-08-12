package com.yuriisurzhykov.pointdetector.data.remote

import com.yuriisurzhykov.pointdetector.data.cache.LatLng

interface DistanceCalculateService {

    fun calculateDistance(coordinates1: LatLng, coordinates2: LatLng): Double

}