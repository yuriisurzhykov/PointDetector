package com.yuriisurzhykov.foodbanks.data.point.cloud

import com.yuriisurzhykov.geo.LatLng
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
    val cityCode: String,
    @SerialName("working_hours")
    val workingHours: List<WorkingHour>,
    /** How to get help: fact type to whether it applies. A missing type means "not known". */
    @SerialName("help")
    val help: Map<String, Boolean> = emptyMap()
)