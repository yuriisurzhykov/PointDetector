package com.yuriisurzhykov.pointdetector.presentation.add

import java.time.DayOfWeek

/** What is wrong with the hours being typed in the sheet, in the order the person should fix it. */
enum class DraftProblem { None, BadTime, NoDays }

/** The hours being typed in the sheet: the chosen days and the two time fields as typed digits. */
data class HoursDraft(
    val days: Set<DayOfWeek> = emptySet(),
    val fromDigits: String = DEFAULT_FROM,
    val toDigits: String = DEFAULT_TO,
) {

    private val from: Int? get() = TimeDigits.minutes(fromDigits)
    private val to: Int? get() = TimeDigits.minutes(toDigits)

    val problem: DraftProblem
        get() {
            val start = from
            val end = to
            return when {
                start == null || end == null || end <= start -> DraftProblem.BadTime
                days.isEmpty() -> DraftProblem.NoDays
                else -> DraftProblem.None
            }
        }

    /** The group to add, or null while there is a [problem]. */
    fun toGroup(): HoursGroup? {
        val start = from
        val end = to
        return if (problem == DraftProblem.None && start != null && end != null) HoursGroup(days, start, end) else null
    }

    fun toggled(day: DayOfWeek): HoursDraft = copy(days = if (day in days) days - day else days + day)

    companion object {
        const val DEFAULT_FROM = "0900"
        const val DEFAULT_TO = "1700"
    }
}
