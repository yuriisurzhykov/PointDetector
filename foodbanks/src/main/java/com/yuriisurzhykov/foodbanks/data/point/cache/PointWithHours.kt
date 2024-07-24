package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.Embedded
import androidx.room.Relation

data class PointWithHours(
    @Embedded
    val point: PointCache,
    @Relation(parentColumn = "pointId", entityColumn = "workingHourId")
    val workingHours: List<WorkingHourCache>
)