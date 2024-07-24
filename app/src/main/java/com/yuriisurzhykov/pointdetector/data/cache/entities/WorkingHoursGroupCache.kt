package com.yuriisurzhykov.pointdetector.data.cache.entities

import kotlinx.serialization.Serializable

@Serializable
data class WorkingHoursGroupCache(
    val workingHours: List<WorkingHoursCache>
)