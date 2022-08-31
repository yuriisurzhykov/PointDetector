package com.yuriisurzhykov.pointdetector.domain.entities

import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem
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
) : Serializable, ViewHolderItem {

    override fun areItemsTheSame(other: Any): Boolean {
        return other is Point && other.address == this.address
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return other is Point
                && other.address == this.address
                && other.workingHours == this.workingHours
                && other.isPointAvailable == this.isPointAvailable
                && other.distance == this.distance
    }

    fun isEmpty(): Boolean {
        return address.isEmpty()
    }

    companion object {
        fun empty() = Point("", LatLng(0.0, 0.0), 0.0, "", emptyList(), false)
    }
}