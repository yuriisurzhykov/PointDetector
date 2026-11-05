package com.yuriisurzhykov.pointdetector.domain.hours

import java.time.DayOfWeek

/** Where a place stands at a given moment, with the data the texts need. */
sealed interface PlaceStanding {

    val isOpen: Boolean get() = this is OpenUntil || this is ClosingSoon

    data class OpenUntil(val untilMinute: Int) : PlaceStanding

    data class ClosingSoon(val minutesLeft: Int) : PlaceStanding

    data class OpensToday(val atMinute: Int) : PlaceStanding

    /** [daysAhead] is 1 for tomorrow. */
    data class ClosedUntil(
        val daysAhead: Int,
        val day: DayOfWeek,
        val startMinute: Int,
        val endMinute: Int,
    ) : PlaceStanding

    data object Unknown : PlaceStanding
}
