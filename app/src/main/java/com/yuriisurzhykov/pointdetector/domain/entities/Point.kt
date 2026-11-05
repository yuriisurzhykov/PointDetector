package com.yuriisurzhykov.pointdetector.domain.entities

import com.google.firebase.database.PropertyName
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Point(
    @PropertyName("address") var address: String = "",
    @PropertyName("coordinates") var coordinates: LatLng = LatLng(0.0, 0.0),
    @PropertyName("placeName") var placeName: String = "",
    @PropertyName("workingHours") var workingHours: List<WeekDay> = emptyList(),
    /** Only the facts the person answered; a kind that is not known is left out. */
    @PropertyName("help") var help: Map<String, Boolean> = emptyMap()
) : Serializable
