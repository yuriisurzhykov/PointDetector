package com.yuriisurzhykov.pointdetector.presentation.add

import java.time.DayOfWeek

/** Some days of the week with the same hours: "Mon-Fri 09:00 - 17:00". */
data class HoursGroup(val days: Set<DayOfWeek>, val fromMinute: Int, val toMinute: Int) {
    init {
        require(days.isNotEmpty() && fromMinute < toMinute) { "A group needs days and a start before the end" }
    }
}
