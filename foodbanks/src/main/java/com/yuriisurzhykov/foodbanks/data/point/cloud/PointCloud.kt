package com.yuriisurzhykov.foodbanks.data.point.cloud

import com.yuriisurzhykov.foodbanks.data.point.LatLng
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PointCloud(
    @SerialName("address")
    val address: String,
    @SerialName("coordinates")
    val coordinates: LatLng,
    @SerialName("place_name")
    val placeName: String,
    @SerialName("working_hours")
    val workingHours: List<WorkingHour>
)