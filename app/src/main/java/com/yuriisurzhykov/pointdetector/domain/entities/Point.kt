package com.yuriisurzhykov.pointdetector.domain.entities

import com.yuriisurzhykov.pointdetector.data.cache.LatLng

data class Point(
    val address: String,
    val coordinates: LatLng,
    val distance: Double
)