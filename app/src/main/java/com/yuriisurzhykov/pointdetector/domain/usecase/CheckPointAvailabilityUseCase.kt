package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.IDateTimeSource
import javax.inject.Inject

interface CheckPointAvailabilityUseCase<T> {

    fun isPointAvailableNow(point: T): Boolean

    open class CachePoint @Inject constructor(
        private val localDateTimeSource: IDateTimeSource.LocalDateTimeSource,
        private val timeAvailabilityUseCase: CheckTimeAvailabilityUseCase
    ) : CheckPointAvailabilityUseCase<PointCache> {

        override fun isPointAvailableNow(point: PointCache): Boolean {
            return point.workingHoursCache.workingHours.any {
                it.workingDay == localDateTimeSource.getDay() && timeAvailabilityUseCase.checkTimeAvailable(
                    it.hoursFrom,
                    it.hoursTo
                )
            }
        }
    }

    open class DomainPoint @Inject constructor(
        private val localDateTimeSource: IDateTimeSource.LocalDateTimeSource,
        private val timeAvailabilityUseCase: CheckTimeAvailabilityUseCase
    ) : CheckPointAvailabilityUseCase<Point> {

        override fun isPointAvailableNow(point: Point): Boolean {
            return point.workingHours.any {
                it.dayName == localDateTimeSource.getDay() && timeAvailabilityUseCase.checkTimeAvailable(
                    it.hoursFrom,
                    it.hoursTo
                )
            }
        }
    }

}