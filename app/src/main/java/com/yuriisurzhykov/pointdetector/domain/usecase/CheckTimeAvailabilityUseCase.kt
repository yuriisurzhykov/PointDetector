package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.domain.source.DateFormatterType
import java.util.*
import javax.inject.Inject

interface CheckTimeAvailabilityUseCase {

    fun checkTimeAvailable(timeFrom: String, timeTo: String): Boolean

    class Base @Inject constructor(
        private val timeFormat: DateFormatterType.TimeFormat
    ) : CheckTimeAvailabilityUseCase {

        override fun checkTimeAvailable(timeFrom: String, timeTo: String): Boolean {
            val calendar = Calendar.getInstance()
            val calendarFromCompare = Calendar.getInstance()
            return try {
                val timeFromDate = timeFormat.getFormatter().parse(timeFrom)
                calendarFromCompare.set(Calendar.HOUR_OF_DAY, timeFromDate.hours)
                calendarFromCompare.set(Calendar.MINUTE, timeFromDate.minutes)
                val calendarToCompare = Calendar.getInstance()
                val timeToDate = timeFormat.getFormatter().parse(timeTo)
                calendarToCompare.set(Calendar.HOUR_OF_DAY, timeToDate.hours)
                calendarToCompare.set(Calendar.MINUTE, timeToDate.minutes)
                calendar.after(calendarFromCompare) && calendar.before(calendarToCompare)
            } catch (e: Exception) {
                false
            }
        }
    }

}