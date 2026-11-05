package com.yuriisurzhykov.pointdetector.uicomponents.preview.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdAxisMark
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdDayOff
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdNextOpening
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdOpenAllDay
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdOpenBetween
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdOpenWithBreak
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdScrubTo
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdSegment
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdWorkingDay
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdDayOffWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdDayTimeline
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdRegularWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdTodayWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdClosedUntil
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdClosingSoon
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdHoursUnknown
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdOpenNow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdOpensToday
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

private const val HOURS_IN_DAY = 24f
private const val MINUTES_IN_HOUR = 60
private val TICKS = listOf(0, 6, 12, 18, 24)

private fun axisMark(position: Float): PdAxisMark {
    val minutes = (position * HOURS_IN_DAY * MINUTES_IN_HOUR).toInt()
    return PdAxisMark(position, pdText("%02d:%02d".format(minutes / MINUTES_IN_HOUR, minutes % MINUTES_IN_HOUR)))
}

private fun ticks() = TICKS.map { PdAxisMark(it / HOURS_IN_DAY, pdText("%02d".format(it))) }

private fun fraction(hour: Int, minute: Int = 0) = (hour + minute / MINUTES_IN_HOUR.toFloat()) / HOURS_IN_DAY

private val workingDay = PdOpenBetween(
    segment = PdSegment(start = fraction(9), end = fraction(17), warnFrom = fraction(16)),
    from = pdText("09:00"),
    until = pdText("17:00"),
)

@PdPreviews
@Composable
private fun PdDayTimelinePreview() {
    PdPreviewFrame {
        var position by remember { mutableFloatStateOf(fraction(16, 20)) }
        PdPreviewLabel("drag or tap the axis: the moment moves, the last hour is shown as closing soon")
        PdDayTimeline(workingDay, axisMark(position), ticks(), PdScrubTo { position = it }).Content(Modifier)
        PdPreviewLabel("read only (PdScrubOff by default)")
        PdDayTimeline(workingDay, axisMark(fraction(11)), ticks()).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdWorkingDayVariantsPreview() {
    PdPreviewFrame {
        val variants: List<Pair<String, PdWorkingDay>> = listOf(
            "PdOpenBetween" to workingDay,
            "PdOpenWithBreak" to PdOpenWithBreak(
                morning = PdSegment(fraction(9), fraction(13)),
                afternoon = PdSegment(fraction(14), fraction(18), warnFrom = fraction(17)),
                summary = pdText("09:00 – 13:00, 14:00 – 18:00"),
            ),
            "PdOpenAllDay" to PdOpenAllDay,
            "PdDayOff" to PdDayOff,
        )
        variants.forEach { (name, day) ->
            PdPreviewLabel("$name: summary comes from the model")
            PdDayTimeline(day, axisMark(fraction(10)), ticks()).Content(Modifier)
        }
    }
}

@PdPreviews
@Composable
private fun PdStatusLineClassesPreview() {
    PdPreviewFrame {
        PdOpenNow(pdText("5 PM")).Content(Modifier)
        PdClosingSoon(minutesLeft = 40).Content(Modifier)
        PdOpensToday(pdText("2 PM")).Content(Modifier)
        PdClosedUntil(PdNextOpening(pdText("Tue"), pdText("09:00 – 12:00"))).Content(Modifier)
        PdHoursUnknown.Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdWeekRowPreview() {
    PdPreviewFrame {
        PdPreviewLabel("PdRegularWeekRow, PdDayOffWeekRow")
        PdRegularWeekRow(pdText("Mon"), pdText("09:00 – 17:00")).Content(Modifier)
        PdDayOffWeekRow(pdText("Sun"), pdText("Day off")).Content(Modifier)
        PdPreviewLabel("PdTodayWeekRow for every status")
        PdTodayWeekRow(pdText("Wed"), pdText("09:00 – 17:00"), PlaceStatus.Open).Content(Modifier)
        PdTodayWeekRow(pdText("Wed"), pdText("09:00 – 17:00"), PlaceStatus.ClosingSoon).Content(Modifier)
        PdTodayWeekRow(pdText("Wed"), pdText("Day off"), PlaceStatus.Closed).Content(Modifier)
    }
}
