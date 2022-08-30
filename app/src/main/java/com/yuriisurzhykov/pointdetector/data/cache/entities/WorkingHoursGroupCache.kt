package com.yuriisurzhykov.pointdetector.data.cache.entities

import androidx.annotation.NonNull
import kotlinx.serialization.Serializable

@Serializable
data class WorkingHoursGroupCache(
    @NonNull
    val workingHours: List<WorkingHoursCache>
)