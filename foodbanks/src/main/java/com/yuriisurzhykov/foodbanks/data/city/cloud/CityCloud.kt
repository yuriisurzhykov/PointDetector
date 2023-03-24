package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud

data class CityCloud(
    val name: String,
    val region: String,
    val nameCode: String,
    val country: String,
    val points: List<PointCloud>
)