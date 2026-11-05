package com.yuriisurzhykov.pointdetector.presentation.points.details

import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.hours.PlaceStanding
import com.yuriisurzhykov.pointdetector.domain.hours.StandingResolver
import com.yuriisurzhykov.pointdetector.domain.hours.WorkInterval
import com.yuriisurzhykov.pointdetector.domain.hours.WorkingHoursParser
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.places.PlaceStatusMapper
import com.yuriisurzhykov.pointdetector.presentation.places.PlacesClock
import com.yuriisurzhykov.pointdetector.presentation.places.help.HelpSectionMapper
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdScrub
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.place.PdPlaceHeader
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.hours.PdStatusHoursPanel
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.section.PdTitledSection
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import javax.inject.Inject

/** Turns a place and a moment into the blocks of the place screen, top to bottom. */
class PlaceDetailsMapper @Inject constructor(
    private val statuses: PlaceStatusMapper,
    private val week: WeekScheduleMapper,
    private val timeline: DayTimelineMapper,
    private val help: HelpSectionMapper,
    private val meta: PlaceMetaMapper,
) {

    private val resolver = StandingResolver()

    fun sections(point: PointUi, clock: PlacesClock, scrub: PdScrub): List<PdComponent> {
        val intervals = intervals(point)
        val standing = standing(intervals, clock)
        val todays = intervals.filter { it.day == clock.day }
        return listOf(
            PdPlaceHeader(PdLiteralText(point.placeName), PdLiteralText(point.address), meta.meta(point)),
            PdStatusHoursPanel(statuses.line(standing), timeline.timeline(todays, clock.minute, scrub)),
            PdTitledSection(
                title = PdStringText(R.string.place_hours_title),
                content = week.schedule(intervals, clock.day, statuses.status(standing)),
            ),
            help.map(point.help),
        )
    }

    /** The colour state of the place at [clock]; the marker on the hero map uses it. */
    fun status(point: PointUi, clock: PlacesClock): PlaceStatus =
        statuses.status(standing(intervals(point), clock))

    private fun standing(intervals: List<WorkInterval>, clock: PlacesClock): PlaceStanding =
        resolver.resolve(intervals, clock.day, clock.minute)

    private fun intervals(point: PointUi): List<WorkInterval> = point.workingHours.mapNotNull {
        WorkingHoursParser.parse(it.dayName, it.hoursFrom, it.hoursTo)
    }
}
