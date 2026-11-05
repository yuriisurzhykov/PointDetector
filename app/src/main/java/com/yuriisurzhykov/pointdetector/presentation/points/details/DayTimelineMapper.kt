package com.yuriisurzhykov.pointdetector.presentation.points.details

import com.yuriisurzhykov.pointdetector.domain.hours.StandingResolver
import com.yuriisurzhykov.pointdetector.domain.hours.WorkInterval
import com.yuriisurzhykov.pointdetector.presentation.places.TimeLabels
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdAxisMark
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdDayOff
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdOpenBetween
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdOpenSegments
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdScrub
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdSegment
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdWorkingDay
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdDayTimeline
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import javax.inject.Inject

private const val TICK_STEP_HOURS = 6
private const val HOURS_IN_DAY = 24

/** The day of a place on the axis: the segments of work, the moment and the hour marks. */
class DayTimelineMapper @Inject constructor() {

    fun timeline(todays: List<WorkInterval>, minute: Int, scrub: PdScrub): PdDayTimeline = PdDayTimeline(
        day = day(todays),
        now = PdAxisMark(AxisTime.fractionOf(minute), TimeLabels.resolver(minute)),
        ticks = (0..HOURS_IN_DAY step TICK_STEP_HOURS).map { hour ->
            PdAxisMark(hour / HOURS_IN_DAY.toFloat(), PdLiteralText(TimeLabels.hourText(hour)))
        },
        scrub = scrub,
    )

    /** Nothing, one stretch or several: the model that matches. The last hour of each is "closing soon". */
    fun day(todays: List<WorkInterval>): PdWorkingDay {
        val sorted = todays.sortedBy { it.startMinute }
        return when (sorted.size) {
            0 -> PdDayOff
            1 -> sorted.single().let {
                PdOpenBetween(segment(it), TimeLabels.resolver(it.startMinute), TimeLabels.resolver(it.endMinute))
            }
            else -> PdOpenSegments(sorted.map(::segment), PdLiteralText(HoursText.of(sorted)))
        }
    }

    private fun segment(interval: WorkInterval): PdSegment {
        val total = WorkInterval.MINUTES_IN_DAY.toFloat()
        val warnFrom = maxOf(interval.startMinute, interval.endMinute - StandingResolver.CLOSING_SOON_MINUTES)
        return PdSegment(interval.startMinute / total, interval.endMinute / total, warnFrom / total)
    }
}
