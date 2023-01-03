package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursCache
import com.yuriisurzhykov.pointsdetector.uicomponents.workday.entity.WeekDay
import javax.inject.Inject

class WeekDayToCacheMapper @Inject constructor() : Mapper<WeekDay, WorkingHoursCache> {
    override fun map(from: WeekDay): WorkingHoursCache {
        return WorkingHoursCache(from.dayName, from.hoursFrom, from.hoursTo)
    }
}