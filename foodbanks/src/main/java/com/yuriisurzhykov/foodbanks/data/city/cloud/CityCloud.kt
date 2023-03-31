package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CityCloud(
    @SerialName("name")
    val name: String,
    @SerialName("region")
    val region: String,
    @SerialName("name_code")
    val nameCode: String,
    @SerialName("country")
    val country: String,
    @SerialName("points")
    val points: List<PointCloud>
)