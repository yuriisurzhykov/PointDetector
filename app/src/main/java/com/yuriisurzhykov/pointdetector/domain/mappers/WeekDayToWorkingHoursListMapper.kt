package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursCache
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay
import javax.inject.Inject

class WeekDayToWorkingHoursListMapper @Inject constructor() : Mapper<List<WeekDay>, List<WorkingHoursCache>> {
    override fun map(from: List<WeekDay>): List<WorkingHoursCache> {
        return from.map { mapSingle(it) }
    }

    private fun mapSingle(from: WeekDay): WorkingHoursCache {
        return WorkingHoursCache(from.dayName, from.hoursFrom, from.hoursTo)
    }
}