package com.yuriisurzhykov.pointdetector.presentation.points.details

import com.yuriisurzhykov.pointdetector.domain.hours.WorkInterval
import java.time.LocalTime
import kotlin.math.roundToInt

private const val STEP_MINUTES = 5
private const val MINUTES_IN_HOUR = 60
private const val LAST_MINUTE = WorkInterval.MINUTES_IN_DAY - STEP_MINUTES

/** Converts between the minute of the day and the fraction of the axis the UI draws. */
object AxisTime {

    fun fractionOf(minute: Int): Float = minute.coerceIn(0, WorkInterval.MINUTES_IN_DAY) / WorkInterval.MINUTES_IN_DAY.toFloat()

    /** The minute under a finger at [fraction] of the axis, snapped to five minutes and kept inside the day. */
    fun minuteOf(fraction: Float): Int {
        val steps = (fraction.coerceIn(0f, 1f) * WorkInterval.MINUTES_IN_DAY / STEP_MINUTES).roundToInt()
        return (steps * STEP_MINUTES).coerceAtMost(LAST_MINUTE)
    }
}

/** The time of day the axis fraction stands for. */
fun Float.toTimeOfDay(): LocalTime {
    val minute = AxisTime.minuteOf(this)
    return LocalTime.of(minute / MINUTES_IN_HOUR, minute % MINUTES_IN_HOUR)
}
