package com.yuriisurzhykov.foodbanks.data.point.user

import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.geo.LatLng

data class UserPoint(
    val cityCode: String,
    val placeName: String,
    val address: String,
    val coordinates: LatLng,
    val workingHours: List<WorkingHour>
)
