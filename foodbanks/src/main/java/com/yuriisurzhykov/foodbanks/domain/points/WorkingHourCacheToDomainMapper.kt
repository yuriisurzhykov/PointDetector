package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.data.point.cache.WorkingHourCache
import javax.inject.Inject

interface WorkingHourCacheToDomainMapper : Mapper<WorkingHourCache, WorkingHour> {

    class Base @Inject constructor() : WorkingHourCacheToDomainMapper,
        Mapper.Abstract<WorkingHourCache, WorkingHour>() {

        override fun map(input: WorkingHourCache) = WorkingHour(
            dayName = input.dayName,
            dayValue = input.dayValue,
            hoursFrom = input.hoursFrom,
            hoursTo = input.hoursTo
        )
    }
}
