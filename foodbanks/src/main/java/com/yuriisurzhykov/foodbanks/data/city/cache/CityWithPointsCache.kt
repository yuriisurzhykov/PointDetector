package com.yuriisurzhykov.foodbanks.data.city.cache

import androidx.room.Embedded
import androidx.room.Relation
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache

data class CityWithPointsCache(
    @Embedded val city: CityCache,
    @Relation(
        parentColumn = "nameCode",
        entityColumn = "cityCodeId"
    )
    var points: List<PointCache> = emptyList()
)