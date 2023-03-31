package com.yuriisurzhykov.foodbanks.data.city.cache

import androidx.room.Embedded
import androidx.room.Relation
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache

data class CityWithPointsCache(
    @Embedded
    val city: CityCache,
    @Relation(parentColumn = "cityId", entityColumn = "cityPointId", entity = PointCache::class)
    val points: List<PointCache>
)