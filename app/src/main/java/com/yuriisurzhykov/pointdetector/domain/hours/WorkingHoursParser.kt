package com.yuriisurzhykov.pointdetector.domain.hours

import java.time.DayOfWeek

/**
 * Reads the hours as they come from the data ("Mon", "09:00", "17:00"). An entry that cannot be
 * read is skipped, so a mistake in the data never turns into an invented opening time.
 */
object WorkingHoursParser {

    private const val MINUTES_IN_HOUR = 60
    private const val HOURS_IN_DAY = 24
    private const val DAY_PREFIX = 3

    private val days = mapOf(
        "mon" to DayOfWeek.MONDAY,
        "tue" to DayOfWeek.TUESDAY,
        "wed" to DayOfWeek.WEDNESDAY,
        "thu" to DayOfWeek.THURSDAY,
        "fri" to DayOfWeek.FRIDAY,
        "sat" to DayOfWeek.SATURDAY,
        "sun" to DayOfWeek.SUNDAY,
    )

    fun parse(dayName: String, from: String, to: String): WorkInterval? {
        val day = days[dayName.trim().lowercase().take(DAY_PREFIX)] ?: return null
        val start = minutes(from) ?: return null
        var end = minutes(to) ?: return null
        if (end == 0 && start > 0) end = WorkInterval.MINUTES_IN_DAY
        if (start >= WorkInterval.MINUTES_IN_DAY || end <= start) return null
        return WorkInterval(day, start, end)
    }

    private fun minutes(text: String): Int? {
        val parts = text.trim().split(':')
        if (parts.size != 2) return null
        val hours = parts[0].toIntOrNull() ?: return null
        val minutes = parts[1].toIntOrNull() ?: return null
        if (hours !in 0..HOURS_IN_DAY || minutes !in 0 until MINUTES_IN_HOUR) return null
        if (hours == HOURS_IN_DAY && minutes != 0) return null
        return hours * MINUTES_IN_HOUR + minutes
    }
}
