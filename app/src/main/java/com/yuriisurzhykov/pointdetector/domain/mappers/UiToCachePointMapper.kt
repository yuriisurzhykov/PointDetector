package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursCache
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursGroupCache
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointsdetector.uicomponents.workday.entity.WeekDay
import javax.inject.Inject

class UiToCachePointMapper @Inject constructor(
    private val workingHoursMapper: Mapper<List<WeekDay>, List<WorkingHoursCache>>
) : Mapper<Point, PointCache> {

    override fun map(from: Point): PointCache {
        return PointCache(
            0,
            from.address,
            from.coordinates,
            from.placeName,
            WorkingHoursGroupCache(workingHoursMapper.map(from.workingHours))
        )
    }
}