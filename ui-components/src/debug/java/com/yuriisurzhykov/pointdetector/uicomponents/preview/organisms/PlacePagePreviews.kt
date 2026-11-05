package com.yuriisurzhykov.pointdetector.uicomponents.preview.organisms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdNumberedMapMarker
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdAxisMark
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdOpenBetween
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdScrubTo
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdSegment
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdFocusOn
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdGeoPoint
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapPlace
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapScene
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdNoUser
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlinedIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.help.PdFactItem
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdDayOffWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdDayTimeline
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdRegularWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdTodayWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.place.PdPlaceHeader
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdOpenNow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.toggles.PdHeartToggle
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.help.PdHelpList
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.hours.PdStatusHoursPanel
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.hours.PdWeekSchedule
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdSketchMapFactory
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdMapHero
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdPlaceActionBar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdPlacePage
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.section.PdTitledSection
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

private const val MINUTES_IN_DAY = 1_440f
private const val OPENING = 9 * 60
private const val CLOSING = 17 * 60
private const val SAMPLE_MINUTE = 980f
private const val SAMPLE_LATITUDE = 38.5767
private const val SAMPLE_LONGITUDE = -121.4738
private val PAGE_HEIGHT = 640.dp

private fun sampleHero(): PdMapHero {
    val point = PdGeoPoint(SAMPLE_LATITUDE, SAMPLE_LONGITUDE)
    val scene = PdMapScene(
        places = listOf(PdMapPlace(1L, point, PdNumberedMapMarker(1, PlaceStatus.Open, selected = true))),
        user = PdNoUser,
        focus = PdFocusOn(point),
        coveredBottom = 0.dp,
        onPlaceClick = {},
    )
    return PdMapHero(
        map = PdSketchMapFactory.create(scene),
        back = PdOutlinedIconButton(PdIcons.Back, pdText("Back")) {},
        share = PdOutlinedIconButton(PdIcons.Share, pdText("Share")) {},
    )
}

@PdPreviews
@Composable
private fun PdMapHeroPreview() {
    PdPreviewFrame { sampleHero().Content(Modifier.fillMaxWidth()) }
}

@PdPreviews
@Composable
private fun PdPlacePagePreview() {
    PdTheme {
        var minute by remember { mutableFloatStateOf(SAMPLE_MINUTE) }
        var favorite by remember { mutableStateOf(false) }
        val clock = "%02d:%02d".format(minute.toInt() / 60, minute.toInt() % 60)
        val day = PdOpenBetween(
            segment = PdSegment(OPENING / MINUTES_IN_DAY, CLOSING / MINUTES_IN_DAY, (CLOSING - 60) / MINUTES_IN_DAY),
            from = pdText("09:00"),
            until = pdText("17:00"),
        )
        val sections = listOf(
            PdPlaceHeader(pdText("Midtown Community Pantry"), pdText("2210 Capitol Ave"), pdText("0.5 km · 6 min on foot")),
            PdStatusHoursPanel(
                status = PdOpenNow(pdText("5:00 PM")),
                timeline = PdDayTimeline(
                    day = day,
                    now = PdAxisMark(minute / MINUTES_IN_DAY, pdText(clock)),
                    ticks = listOf(0, 6, 12, 18, 24).map { PdAxisMark(it / 24f, pdText("%02d".format(it))) },
                    scrub = PdScrubTo { minute = (it * MINUTES_IN_DAY).coerceAtMost(MINUTES_IN_DAY - 1f) },
                ),
            ),
            PdTitledSection(
                title = pdText("Working hours"),
                content = PdWeekSchedule(
                    listOf(
                        PdRegularWeekRow(pdText("Monday"), pdText("09:00 – 17:00")),
                        PdTodayWeekRow(pdText("Tuesday"), pdText("09:00 – 17:00"), PlaceStatus.Open),
                        PdDayOffWeekRow(pdText("Sunday"), pdText("Day off")),
                    ),
                ),
            ),
            PdHelpList(
                pdText("How to get help"),
                listOf(PdFactItem(PdIcons.Calendar, pdText("Appointment"), pdText("Not required"))),
            ),
        )
        PdPlacePage(
            hero = sampleHero(),
            sections = sections,
            actionBar = PdPlaceActionBar(
                primary = PdPrimaryButton(pdText("Get directions"), PdDecorativeIcon(PdIcons.Route)) {},
                favorite = PdHeartToggle(favorite, pdText("Add to favorites")) { favorite = it },
            ),
        ).Content(Modifier.fillMaxWidth().height(PAGE_HEIGHT))
    }
}
