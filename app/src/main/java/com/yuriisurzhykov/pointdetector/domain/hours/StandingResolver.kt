package com.yuriisurzhykov.pointdetector.domain.hours

import java.time.DayOfWeek

/** Decides if a place is open, about to close, or closed, from its hours and the current moment. */
class StandingResolver(private val closingSoonMinutes: Int = CLOSING_SOON_MINUTES) {

    fun resolve(intervals: List<WorkInterval>, today: DayOfWeek, minute: Int): PlaceStanding {
        if (intervals.isEmpty()) return PlaceStanding.Unknown
        val todays = intervals.filter { it.day == today }.sortedBy { it.startMinute }
        todays.firstOrNull { it.contains(minute) }?.let { current ->
            val left = current.endMinute - minute
            return if (left <= closingSoonMinutes) {
                PlaceStanding.ClosingSoon(left)
            } else {
                PlaceStanding.OpenUntil(current.endMinute)
            }
        }
        todays.firstOrNull { it.startMinute > minute }?.let { return PlaceStanding.OpensToday(it.startMinute) }
        for (ahead in 1..DAYS_IN_WEEK) {
            val day = today.plus(ahead.toLong())
            val next = intervals.filter { it.day == day }.minByOrNull { it.startMinute }
            if (next != null) return PlaceStanding.ClosedUntil(ahead, day, next.startMinute, next.endMinute)
        }
        return PlaceStanding.Unknown
    }

    companion object {
        const val CLOSING_SOON_MINUTES = 60
        private const val DAYS_IN_WEEK = 7
    }
}
