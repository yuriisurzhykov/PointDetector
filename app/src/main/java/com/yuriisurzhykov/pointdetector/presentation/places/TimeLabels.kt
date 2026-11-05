package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import java.time.LocalTime
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale

private const val HOURS_IN_DAY = 24
private val MINUTES_PART = Regex("[:.]mm")
private const val SECONDS_IN_MINUTE = 60
private const val MINUTES_IN_DAY = 24 * 60

/** A time of day written the way the user's locale writes it ("5:00 PM" or "17:00"). */
object TimeLabels {

    fun text(minuteOfDay: Int): String {
        val time = LocalTime.ofSecondOfDay((minuteOfDay % MINUTES_IN_DAY) * SECONDS_IN_MINUTE.toLong())
        return time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault()))
    }

    fun resolver(minuteOfDay: Int): PdTextResolver = PdLiteralText(text(minuteOfDay))

    /**
     * A whole hour for the marks of an axis: the short time pattern of the locale without its
     * minutes ("6 AM" or "06"). The end of the day, 24, is written as the midnight it is.
     */
    fun hourText(hour: Int, locale: Locale = Locale.getDefault()): String {
        val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(null, FormatStyle.SHORT, IsoChronology.INSTANCE, locale)
        val withoutMinutes = pattern.replace(MINUTES_PART, "")
        return LocalTime.of(hour % HOURS_IN_DAY, 0).format(DateTimeFormatter.ofPattern(withoutMinutes, locale))
    }
}
