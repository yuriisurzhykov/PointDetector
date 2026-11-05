package com.yuriisurzhykov.pointdetector.presentation.entities

import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.domain.entities.WeekDay
import java.io.Serializable

@kotlinx.serialization.Serializable
data class PointUi(
    val address: String,
    val coordinates: LatLng,
    val distanceString: String,
    val distanceDouble: Double,
    val placeName: String,
    var workingHours: List<WeekDay> = emptyList(),
    val isPointAvailable: Boolean = false,
    val favoriteState: FavoriteState,
    val id: Long = 0,
    val isUserPoint: Boolean = false,
    val help: Map<String, Boolean> = emptyMap()
) : Serializable {

    fun isEmpty(): Boolean {
        return address.isEmpty()
    }
}
