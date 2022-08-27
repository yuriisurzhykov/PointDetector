package com.yuriisurzhykov.pointdetector.domain.entities

import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointsdetector.uicomponents.WeekDay
import java.io.Serializable

data class Point(
    val address: String,
    val coordinates: LatLng,
    val distance: Double,
    val placeName: String,
    val workingHours: List<WeekDay>
) : Serializable {

    fun isEmpty(): Boolean {
        return address.isEmpty()
    }
}