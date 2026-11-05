package com.yuriisurzhykov.pointdetector.presentation.points.details

import com.yuriisurzhykov.pointdetector.domain.hours.WorkInterval
import com.yuriisurzhykov.pointdetector.presentation.places.help.AppointmentFactFactory
import com.yuriisurzhykov.pointdetector.presentation.places.help.DocumentsFactFactory
import com.yuriisurzhykov.pointdetector.presentation.places.help.HelpSectionMapper
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdDayOff
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdOpenBetween
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdOpenSegments
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdDayOffWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdRegularWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdTodayWeekRow
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.help.PdHelpList
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.help.PdNoHelpSection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.util.Locale

class PlaceDetailsMappersTest {

    private val week = WeekScheduleMapper()
    private val timeline = DayTimelineMapper()
    private val help = HelpSectionMapper.Base(setOf(AppointmentFactFactory(), DocumentsFactFactory()))

    private fun interval(day: DayOfWeek, from: Int, to: Int) = WorkInterval(day, from * 60, to * 60)

    // ---- the week ----

    @Test
    fun `test the week has seven rows starting on Monday`() {
        val rows = week.rows(emptyList(), DayOfWeek.WEDNESDAY, PlaceStatus.Closed, Locale.ENGLISH)

        assertEquals(7, rows.size)
        assertTrue(rows[2] is PdTodayWeekRow)
    }

    @Test
    fun `test today is highlighted even when it is a day off`() {
        val rows = week.rows(listOf(interval(DayOfWeek.MONDAY, 9, 17)), DayOfWeek.SUNDAY, PlaceStatus.Closed, Locale.ENGLISH)

        assertTrue(rows[6] is PdTodayWeekRow)
    }

    @Test
    fun `test days with hours are regular rows and the others are days off`() {
        val rows = week.rows(
            listOf(interval(DayOfWeek.TUESDAY, 10, 19), interval(DayOfWeek.THURSDAY, 10, 19)),
            DayOfWeek.TUESDAY,
            PlaceStatus.Open,
            Locale.ENGLISH,
        )

        assertEquals(7, rows.size)
        assertTrue(rows[0] is PdDayOffWeekRow)
        assertTrue(rows[1] is PdTodayWeekRow)
        assertTrue(rows[3] is PdRegularWeekRow)
        assertTrue(rows[6] is PdDayOffWeekRow)
    }

    @Test
    fun `test several stretches of one day are written in order with a comma`() {
        val text = HoursText.of(
            listOf(interval(DayOfWeek.MONDAY, 14, 17), interval(DayOfWeek.MONDAY, 9, 12)),
        )

        assertTrue(text.indexOf(",") > 0)
        assertTrue(text.contains("–"))
    }

    // ---- the day axis ----

    @Test
    fun `test the axis fraction becomes a time snapped to five minutes`() {
        assertEquals(0, AxisTime.minuteOf(0f))
        assertEquals(12 * 60, AxisTime.minuteOf(0.5f))
        assertEquals(24 * 60 - 5, AxisTime.minuteOf(1f))
        assertEquals(java.time.LocalTime.of(12, 0), 0.5f.toTimeOfDay())
    }

    @Test
    fun `test a position outside the axis stays inside the day`() {
        assertEquals(0, AxisTime.minuteOf(-3f))
        assertEquals(24 * 60 - 5, AxisTime.minuteOf(7f))
    }

    @Test
    fun `test the model of a day matches the number of stretches`() {
        assertTrue(timeline.day(emptyList()) === PdDayOff)
        assertTrue(timeline.day(listOf(interval(DayOfWeek.MONDAY, 9, 17))) is PdOpenBetween)
        assertTrue(
            timeline.day(listOf(interval(DayOfWeek.MONDAY, 9, 12), interval(DayOfWeek.MONDAY, 14, 17))) is PdOpenSegments,
        )
    }

    @Test
    fun `test the last hour of a stretch is marked as closing soon`() {
        val day = timeline.day(listOf(interval(DayOfWeek.MONDAY, 9, 17))) as PdOpenBetween
        val segment = day.segments.single()

        assertEquals(16 * 60 / 1440f, segment.warnFrom, 0.0001f)
        assertEquals(17 * 60 / 1440f, segment.end, 0.0001f)
    }

    @Test
    fun `test a short stretch is closing soon from its very start`() {
        val day = timeline.day(listOf(WorkInterval(DayOfWeek.MONDAY, 9 * 60, 9 * 60 + 30))) as PdOpenBetween

        assertEquals(day.segments.single().start, day.segments.single().warnFrom, 0.0001f)
    }

    // ---- how to get help ----

    @Test
    fun `test known facts become rows in the order of the data`() {
        val items = help.items(linkedMapOf("documents" to true, "appointment" to false))

        assertEquals(2, items.size)
    }

    @Test
    fun `test an unknown kind of fact is skipped`() {
        assertEquals(1, help.items(mapOf("delivery" to true, "appointment" to true)).size)
    }

    @Test
    fun `test no facts or only unknown ones give no section at all`() {
        assertTrue(help.map(emptyMap()) === PdNoHelpSection)
        assertTrue(help.map(mapOf("delivery" to true)) === PdNoHelpSection)
    }

    @Test
    fun `test known facts give the list`() {
        assertTrue(help.map(mapOf("appointment" to true)) is PdHelpList)
    }

    // ---- the distances ----

    @Test
    fun `test walking and driving minutes come from meters and are at least one`() {
        val meta = PlaceMetaMapper()

        assertEquals(6, meta.walkMinutes(500.0))
        assertEquals(2, meta.driveMinutes(500.0))
        assertEquals(1, meta.walkMinutes(10.0))
        assertEquals(1, meta.driveMinutes(10.0))
    }
}
