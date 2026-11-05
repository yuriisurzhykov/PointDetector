package com.yuriisurzhykov.pointdetector.domain.entities

import kotlinx.serialization.Serializable

/** One working day of a place as the data keeps it: "Mon", a day value and the "09:00" / "17:00" of the hours. */
@Serializable
data class WeekDay(
    var dayName: String = "",
    var dayValue: Int = 0,
    var hoursFrom: String = "",
    var hoursTo: String = ""
) : java.io.Serializable
