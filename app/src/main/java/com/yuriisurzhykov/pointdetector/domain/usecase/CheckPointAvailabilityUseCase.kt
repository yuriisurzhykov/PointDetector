package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.IDateTimeSource
import javax.inject.Inject

interface CheckPointAvailabilityUseCase {

    fun isPointAvailableNow(point: Point): Boolean

    open class Base @Inject constructor(
        private val localDateTimeSource: IDateTimeSource.LocalDateTimeSource
    ) : CheckPointAvailabilityUseCase {

        override fun isPointAvailableNow(point: Point): Boolean {
//            val isDayIncluded =
            return false
        }
    }

}