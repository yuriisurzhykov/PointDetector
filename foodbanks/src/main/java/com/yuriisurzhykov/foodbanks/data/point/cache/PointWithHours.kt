package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.Embedded
import androidx.room.Relation
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritePointCache

data class PointWithHours(
    @Embedded
    val point: PointCache,
    @Relation(parentColumn = "pointId", entityColumn = "pointId")
    val workingHours: List<WorkingHourCache>,
    @Relation(parentColumn = "pointId", entityColumn = "pointId")
    val favorites: List<FavoritePointCache> = emptyList(),
    @Relation(parentColumn = "pointId", entityColumn = "pointId")
    val helpFacts: List<HelpFactCache> = emptyList()
)
