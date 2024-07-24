package com.yuriisurzhykov.pointdetector.presentation.entities

import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderItem
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay
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
    val favoriteState: FavoriteState
) : Serializable, ViewHolderItem {

    override fun areItemsTheSame(other: Any): Boolean {
        return other is PointUi && other.address == this.address
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return other is PointUi
                && other.placeName == this.placeName
                && other.address == this.address
                && other.workingHours == this.workingHours
                && other.isPointAvailable == this.isPointAvailable
                && other.distanceString == this.distanceString
                && other.favoriteState == this.favoriteState
    }

    fun isEmpty(): Boolean {
        return address.isEmpty()
    }
}