package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.data.point.cache.PointWithHours
import com.yuriisurzhykov.foodbanks.data.point.cache.WorkingHourCache
import javax.inject.Inject

interface PointCacheToDomainMapper : Mapper.List<PointWithHours, Point> {

    class Base @Inject constructor(
        private val workingHourMapper: Mapper<WorkingHourCache, WorkingHour>
    ) : Mapper.AbstractList<PointWithHours, Point>(), PointCacheToDomainMapper {

        override fun mapSingle(input: PointWithHours): Point = Point.Base(
            input.point.pointId,
            input.point.placeName,
            input.point.address,
            input.point.coordinates,
            input.workingHours.map { workingHourMapper.map(it) },
            input.point.origin,
            input.favorites.isNotEmpty()
        )
    }
}
