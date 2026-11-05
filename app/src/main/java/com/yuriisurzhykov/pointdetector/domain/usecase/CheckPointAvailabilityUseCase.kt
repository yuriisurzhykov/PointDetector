package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.pointdetector.domain.services.IDateTimeSource
import javax.inject.Inject

interface CheckPointAvailabilityUseCase<T> {

    fun isPointAvailableNow(point: T): Boolean

    open class WorkingHours @Inject constructor(
        private val localDateTimeSource: IDateTimeSource.LocalDateTimeSource,
        private val timeAvailabilityUseCase: CheckTimeAvailabilityUseCase
    ) : CheckPointAvailabilityUseCase<List<WorkingHour>> {

        override fun isPointAvailableNow(point: List<WorkingHour>): Boolean {
            return point.any {
                it.dayName == localDateTimeSource.getDay() && timeAvailabilityUseCase.checkTimeAvailable(
                    it.hoursFrom,
                    it.hoursTo
                )
            }
        }
    }

}
