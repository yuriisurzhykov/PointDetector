package com.yuriisurzhykov.pointdetector.presentation.points.details

import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.hours.WorkInterval
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdDayOffWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdRegularWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdTodayWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.hours.PdWeekSchedule
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

/** Seven rows, Monday first: today is highlighted, a day without hours says it is a day off. */
class WeekScheduleMapper @Inject constructor() {

    fun schedule(
        intervals: List<WorkInterval>,
        today: DayOfWeek,
        status: PlaceStatus,
        locale: Locale = Locale.getDefault(),
    ): PdWeekSchedule = PdWeekSchedule(rows(intervals, today, status, locale))

    fun rows(
        intervals: List<WorkInterval>,
        today: DayOfWeek,
        status: PlaceStatus,
        locale: Locale = Locale.getDefault(),
    ): List<PdWeekRow> = DayOfWeek.entries.map { day ->
        val ofDay = intervals.filter { it.day == day }
        val name = PdLiteralText(dayName(day, locale))
        val hours = if (ofDay.isEmpty()) PdStringText(R.string.place_day_off) else PdLiteralText(HoursText.of(ofDay))
        when {
            day == today -> PdTodayWeekRow(name, hours, status)
            ofDay.isEmpty() -> PdDayOffWeekRow(name, hours)
            else -> PdRegularWeekRow(name, hours)
        }
    }

    private fun dayName(day: DayOfWeek, locale: Locale): String =
        day.getDisplayName(TextStyle.FULL_STANDALONE, locale).replaceFirstChar { it.titlecase(locale) }
}
