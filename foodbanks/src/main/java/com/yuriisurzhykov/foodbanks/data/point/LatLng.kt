package com.yuriisurzhykov.foodbanks.data.point

@kotlinx.serialization.Serializable
data class LatLng(
    val lat: Double,
    val lng: Double
)