package com.yuriisurzhykov.pointdetector.domain.hours

import java.time.DayOfWeek

/** One stretch of work on a day of the week, in minutes from midnight. The end is exclusive. */
data class WorkInterval(val day: DayOfWeek, val startMinute: Int, val endMinute: Int) {

    init {
        require(startMinute in 0 until MINUTES_IN_DAY && endMinute in (startMinute + 1)..MINUTES_IN_DAY) {
            "A work interval needs 0 <= start < end <= 24:00, got $startMinute..$endMinute"
        }
    }

    fun contains(minute: Int): Boolean = minute in startMinute until endMinute

    companion object {
        const val MINUTES_IN_DAY = 24 * 60
    }
}
