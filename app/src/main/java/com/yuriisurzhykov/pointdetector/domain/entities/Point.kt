package com.yuriisurzhykov.pointdetector.domain.entities

import com.google.firebase.database.PropertyName
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem
import com.yuriisurzhykov.pointsdetector.uicomponents.workday.entity.WeekDay
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Point(
    @PropertyName("address") var address: String = "",
    @PropertyName("coordinates") var coordinates: LatLng = LatLng(0.0, 0.0),
    @PropertyName("distance") var distance: Double = 0.0,
    @PropertyName("placeName") var placeName: String = "",
    @PropertyName("workingHours") var workingHours: List<WeekDay> = emptyList(),
    @PropertyName("isPointAvailable") var isPointAvailable: Boolean = false
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