package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursGroupCache
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import javax.inject.Inject

class PointUiToCacheMapper @Inject constructor(
    private val mapper: WeekDayToCacheMapper
) : Mapper<PointUi, PointCache> {
    override fun map(from: PointUi): PointCache {
        return PointCache(
            0,
            from.address,
            from.coordinates,
            from.placeName,
            WorkingHoursGroupCache(from.workingHours.map { mapper.map(it) })
        )
    }
}