package com.yuriisurzhykov.pointdetector.data.cache.entities

import com.google.firebase.database.PropertyName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class LatLng(
    @PropertyName("lat") var lat: Double = 0.0,
    @PropertyName("lng") var lng: Double = 0.0
) : Serializable