package com.yuriisurzhykov.pointdetector.presentation.add

import com.yuriisurzhykov.pointdetector.domain.entities.WeekDay
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

private const val MIN_RUN_TO_JOIN = 3

/** The groups of hours of a new place: adding, removing, writing them down and turning them into data. */
object HoursGroups {

    /** Adds [group]; a day that was in another group moves to the new one, so a day never has two hours. */
    fun add(groups: List<HoursGroup>, group: HoursGroup): List<HoursGroup> =
        (groups.mapNotNull { it.without(group.days) } + group).sortedBy { it.days.minOf(DayOfWeek::getValue) }

    fun remove(groups: List<HoursGroup>, index: Int): List<HoursGroup> =
        groups.filterIndexed { position, _ -> position != index }

    /** One entry per day, in the form the data keeps: "Mon", 1, "09:00", "17:00". */
    fun toWeekDays(groups: List<HoursGroup>): List<WeekDay> =
        groups.flatMap { group ->
            group.days.map { day ->
                WeekDay(
                    dayName = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    dayValue = day.value,
                    hoursFrom = TimeDigits.clock(group.fromMinute),
                    hoursTo = TimeDigits.clock(group.toMinute),
                )
            }
        }.sortedBy { it.dayValue }

    /** "Mon-Wed, Fri": a run of three or more days is joined, shorter ones are listed. */
    fun daysLabel(days: Set<DayOfWeek>, locale: Locale = Locale.getDefault()): String =
        runs(days.sortedBy { it.value }).joinToString(", ") { run ->
            if (run.size >= MIN_RUN_TO_JOIN) "${name(run.first(), locale)}–${name(run.last(), locale)}" else run.joinToString(", ") { name(it, locale) }
        }

    private fun HoursGroup.without(taken: Set<DayOfWeek>): HoursGroup? =
        (days - taken).takeIf { it.isNotEmpty() }?.let { copy(days = it) }

    private fun runs(sorted: List<DayOfWeek>): List<List<DayOfWeek>> {
        val result = mutableListOf<MutableList<DayOfWeek>>()
        sorted.forEach { day ->
            val last = result.lastOrNull()
            if (last != null && last.last().value + 1 == day.value) last += day else result += mutableListOf(day)
        }
        return result
    }

    fun name(day: DayOfWeek, locale: Locale = Locale.getDefault()): String =
        day.getDisplayName(TextStyle.SHORT_STANDALONE, locale).replaceFirstChar { it.titlecase(locale) }
}
