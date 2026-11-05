package com.yuriisurzhykov.pointdetector.uicomponents.preview.organisms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdAxisMark
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdOpenBetween
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdScrubTo
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdSegment
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdDayOffWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdDayTimeline
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdRegularWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdTodayWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdClosingSoon
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdOpenNow
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.hours.PdStatusHoursPanel
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.hours.PdWeekSchedule
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.section.PdTitledSection
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

private const val HOURS_IN_DAY = 24f
private const val MINUTES_IN_DAY = 1_440
private const val MINUTES_IN_HOUR = 60
private const val OPENING_HOUR = 9
private const val CLOSING_HOUR = 17
private const val LAST_HOUR_MINUTES = 60
private val DAY_NAMES = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
private const val TODAY_INDEX = 2

private fun fraction(hour: Int) = hour / HOURS_IN_DAY

@PdPreviews
@Composable
private fun PdWeekSchedulePreview() {
    PdPreviewFrame {
        PdWeekSchedule(
            DAY_NAMES.mapIndexed { index, name ->
                when {
                    index == TODAY_INDEX -> PdTodayWeekRow(pdText(name), pdText("09:00 – 17:00"), PlaceStatus.Open)
                    index >= DAY_NAMES.size - 2 -> PdDayOffWeekRow(pdText(name), pdText("Day off"))
                    else -> PdRegularWeekRow(pdText(name), pdText("09:00 – 17:00"))
                }
            },
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdStatusHoursPanelPreview() {
    PdPreviewFrame {
        var minute by remember { mutableFloatStateOf(980f) }
        val day = PdOpenBetween(
            segment = PdSegment(
                start = fraction(OPENING_HOUR),
                end = fraction(CLOSING_HOUR),
                warnFrom = (CLOSING_HOUR * MINUTES_IN_HOUR - LAST_HOUR_MINUTES) / MINUTES_IN_DAY.toFloat(),
            ),
            from = pdText("09:00"),
            until = pdText("17:00"),
        )
        val position = minute / MINUTES_IN_DAY
        val closing = minute >= (CLOSING_HOUR * MINUTES_IN_HOUR - LAST_HOUR_MINUTES)
        val clock = "%02d:%02d".format(minute.toInt() / MINUTES_IN_HOUR, minute.toInt() % MINUTES_IN_HOUR)
        PdPreviewLabel("drag the axis: the status follows the moment")
        PdStatusHoursPanel(
            status = if (closing) PdClosingSoon(CLOSING_HOUR * MINUTES_IN_HOUR - minute.toInt()) else PdOpenNow(pdText("5 PM")),
            timeline = PdDayTimeline(
                day = day,
                now = PdAxisMark(position, pdText(clock)),
                ticks = listOf(0, 6, 12, 18, 24).map { PdAxisMark(it / HOURS_IN_DAY, pdText("%02d".format(it))) },
                scrub = PdScrubTo { minute = (it * MINUTES_IN_DAY).coerceAtMost(MINUTES_IN_DAY - 1f) },
            ),
        ).Content(Modifier)
    }
}

@PdPreviews
@Composable
private fun PdTitledSectionPreview() {
    PdPreviewFrame {
        PdTitledSection(
            title = pdText("Working hours"),
            content = PdWeekSchedule(
                listOf(
                    PdRegularWeekRow(pdText("Mon"), pdText("09:00 – 17:00")),
                    PdDayOffWeekRow(pdText("Sun"), pdText("Day off")),
                ),
            ),
        ).Content(Modifier)
    }
}
