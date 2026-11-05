package com.yuriisurzhykov.pointdetector.presentation.points.details

import com.yuriisurzhykov.pointdetector.domain.hours.WorkInterval
import com.yuriisurzhykov.pointdetector.presentation.places.TimeLabels

/** The hours of a day as one line: "9:00 AM – 5:00 PM", with a comma between several stretches. */
object HoursText {

    fun of(intervals: List<WorkInterval>): String =
        intervals.sortedBy { it.startMinute }.joinToString(SEPARATOR) { range(it) }

    fun range(interval: WorkInterval): String =
        "${TimeLabels.text(interval.startMinute)} – ${TimeLabels.text(interval.endMinute)}"

    private const val SEPARATOR = ", "
}
