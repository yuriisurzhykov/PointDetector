package com.yuriisurzhykov.pointdetector.data.cache.entities

import kotlinx.serialization.Serializable

@Serializable
data class WorkingHoursCache(
    val workingDay: String = "",
    val hoursFrom: String = "",
    val hoursTo: String = ""
)