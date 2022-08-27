package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.IDateTimeSource
import javax.inject.Inject

interface CheckPointAvailabilityUseCase {

    fun isPointAvailableNow(point: PointCache): Boolean

    open class Base @Inject constructor(
        private val localDateTimeSource: IDateTimeSource.LocalDateTimeSource,
        private val timeAvailabilityUseCase: CheckTimeAvailabilityUseCase
    ) : CheckPointAvailabilityUseCase {

        override fun isPointAvailableNow(point: PointCache): Boolean {
            return point.workingHoursCache.workingHours.any {
                it.workingDay == localDateTimeSource.getDay() && timeAvailabilityUseCase.checkTimeAvailable(
                    it.hoursFrom,
                    it.hoursTo
                )
            }
        }
    }

}