package com.yuriisurzhykov.pointdetector.domain.entities

import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.domain.usecase.CheckPointAvailabilityUseCase
import com.yuriisurzhykov.pointsdetector.uicomponents.WeekDay
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Point(
    val address: String,
    val coordinates: LatLng,
    val distance: Double,
    val placeName: String,
    val workingHours: List<WeekDay>,
    val isPointAvailable: Boolean
) : Serializable {

    fun isEmpty(): Boolean {
        return address.isEmpty()
    }

    companion object {
        fun empty() = Point("", LatLng(0.0, 0.0), 0.0, "", emptyList(), false)
    }
}