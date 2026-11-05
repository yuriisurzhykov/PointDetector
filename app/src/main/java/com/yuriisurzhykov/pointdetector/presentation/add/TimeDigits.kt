package com.yuriisurzhykov.pointdetector.presentation.add

private const val DIGITS = 4
private const val HOURS_DIGITS = 2
private const val MINUTES_IN_HOUR = 60
private const val HOURS_IN_DAY = 24

/** Reads and writes the four typed digits of a time field ("0900" is 09:00), in minutes of the day. */
object TimeDigits {

    /** The minute of the day the digits stand for, or null when they are not a time. "2400" is the end of the day. */
    fun minutes(digits: String): Int? {
        if (digits.length != DIGITS || !digits.all(Char::isDigit)) return null
        val hours = digits.substring(0, HOURS_DIGITS).toInt()
        val minutes = digits.substring(HOURS_DIGITS).toInt()
        if (hours > HOURS_IN_DAY || minutes >= MINUTES_IN_HOUR) return null
        if (hours == HOURS_IN_DAY && minutes != 0) return null
        return hours * MINUTES_IN_HOUR + minutes
    }

    fun digits(minute: Int): String = "%02d%02d".format(minute / MINUTES_IN_HOUR, minute % MINUTES_IN_HOUR)

    /** "09:00": the form the data keeps. */
    fun clock(minute: Int): String = "%02d:%02d".format(minute / MINUTES_IN_HOUR, minute % MINUTES_IN_HOUR)
}
