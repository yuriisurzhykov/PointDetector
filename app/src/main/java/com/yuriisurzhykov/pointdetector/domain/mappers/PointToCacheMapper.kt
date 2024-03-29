package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursGroupCache
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

class PointToCacheMapper @Inject constructor(
    private val mapper: WeekDayToCacheMapper
) : Mapper<Point, PointCache> {
    override fun map(from: Point): PointCache {
        return PointCache(
            0,
            from.address,
            from.coordinates,
            from.placeName,
            WorkingHoursGroupCache(from.workingHours.map { mapper.map(it) }),
            false
        )
    }
}