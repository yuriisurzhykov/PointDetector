package com.yuriisurzhykov.pointdetector.data.cache.entities

import java.io.Serializable

@kotlinx.serialization.Serializable
data class LatLng(
    val lat: Double,
    val lng: Double
) : Serializable