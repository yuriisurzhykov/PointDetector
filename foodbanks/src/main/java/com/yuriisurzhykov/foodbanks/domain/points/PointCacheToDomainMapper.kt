package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache

interface PointCacheToDomainMapper : Mapper.List<PointCache, Point> {

    class Base : Mapper.AbstractList<PointCache, Point>(), PointCacheToDomainMapper {

        override fun map(input: PointCache): Point = Point.Base(
            input.pointId,
            input.placeName,
            input.address,
            input.workingHours
        )
    }
}