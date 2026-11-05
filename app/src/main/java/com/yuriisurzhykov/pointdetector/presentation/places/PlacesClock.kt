package com.yuriisurzhykov.pointdetector.presentation.places

import java.time.DayOfWeek
import java.time.LocalDateTime

private const val MINUTES_IN_HOUR = 60
private const val MILLIS_IN_MINUTE = 60_000L

/** The moment the statuses are computed for: the day and the minute of the day. */
data class PlacesClock(val day: DayOfWeek, val minute: Int) {

    companion object {
        fun now(): PlacesClock {
            val now = LocalDateTime.now()
            return PlacesClock(now.dayOfWeek, now.hour * MINUTES_IN_HOUR + now.minute)
        }

        fun millisToNextMinute(): Long = MILLIS_IN_MINUTE - System.currentTimeMillis() % MILLIS_IN_MINUTE
    }
}
