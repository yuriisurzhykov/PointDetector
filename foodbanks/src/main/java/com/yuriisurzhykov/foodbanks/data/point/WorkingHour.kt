package com.yuriisurzhykov.foodbanks.data.point

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class WorkingHour(
    @SerialName("dayName")
    val dayName: String,
    @SerialName("dayValue")
    val dayValue: Int,
    @SerialName("hoursFrom")
    val hoursFrom: String,
    @SerialName("hoursTo")
    val hoursTo: String
)