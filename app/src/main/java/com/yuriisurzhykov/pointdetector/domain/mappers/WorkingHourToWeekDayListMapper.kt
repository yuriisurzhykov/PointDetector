package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursCache
import com.yuriisurzhykov.pointdetector.domain.services.WeekDaysListResource
import com.yuriisurzhykov.pointsdetector.uicomponents.WeekDay
import javax.inject.Inject

class WorkingHourToWeekDayListMapper @Inject constructor(
    private val weekDaysListResource: WeekDaysListResource
) : Mapper<List<WorkingHoursCache>, List<WeekDay>> {

    override fun map(from: List<WorkingHoursCache>): List<WeekDay> {
        return from.map { mapSingle(it) }
    }

    private fun mapSingle(from: WorkingHoursCache): WeekDay {
        return WeekDay(
            from.workingDay,
            weekDaysListResource.getDaysAsList().indexOf(from.workingDay),
            from.hoursFrom,
            from.hoursTo
        )
    }
}