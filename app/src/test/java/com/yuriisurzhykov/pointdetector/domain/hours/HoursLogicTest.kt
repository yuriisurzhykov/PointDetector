package com.yuriisurzhykov.pointdetector.domain.hours

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek

class HoursLogicTest {

    private val resolver = StandingResolver()

    private fun interval(day: DayOfWeek, from: String, to: String): WorkInterval =
        WorkingHoursParser.parse(day.name.take(3), from, to)!!

    private fun at(hours: Int, minutes: Int = 0) = hours * 60 + minutes

    // ---- parser ----

    @Test
    fun `test hours are read with the day name of the data`() {
        val parsed = WorkingHoursParser.parse("Mon", "11:30", "15:00")

        assertEquals(WorkInterval(DayOfWeek.MONDAY, at(11, 30), at(15)), parsed)
    }

    @Test
    fun `test midnight as the end means the end of the day`() {
        assertEquals(WorkInterval.MINUTES_IN_DAY, WorkingHoursParser.parse("Fri", "18:00", "00:00")!!.endMinute)
        assertEquals(WorkInterval.MINUTES_IN_DAY, WorkingHoursParser.parse("Fri", "18:00", "24:00")!!.endMinute)
    }

    @Test
    fun `test entries that cannot be read are skipped instead of invented`() {
        assertNull(WorkingHoursParser.parse("Funday", "09:00", "17:00"))
        assertNull(WorkingHoursParser.parse("Mon", "9", "17:00"))
        assertNull(WorkingHoursParser.parse("Mon", "09:00", "25:00"))
        assertNull(WorkingHoursParser.parse("Mon", "09:60", "17:00"))
        assertNull(WorkingHoursParser.parse("Mon", "17:00", "09:00"))
        assertNull(WorkingHoursParser.parse("Mon", "09:00", "09:00"))
    }

    // ---- resolver ----

    @Test
    fun `test a place without hours is unknown`() {
        assertEquals(PlaceStanding.Unknown, resolver.resolve(emptyList(), DayOfWeek.MONDAY, at(12)))
    }

    @Test
    fun `test open until the end of the interval`() {
        val hours = listOf(interval(DayOfWeek.MONDAY, "09:00", "17:00"))

        assertEquals(PlaceStanding.OpenUntil(at(17)), resolver.resolve(hours, DayOfWeek.MONDAY, at(12)))
    }

    @Test
    fun `test the last hour is closing soon and the minutes are counted`() {
        val hours = listOf(interval(DayOfWeek.MONDAY, "09:00", "17:00"))

        assertEquals(PlaceStanding.ClosingSoon(60), resolver.resolve(hours, DayOfWeek.MONDAY, at(16)))
        assertEquals(PlaceStanding.ClosingSoon(40), resolver.resolve(hours, DayOfWeek.MONDAY, at(16, 20)))
        assertEquals(PlaceStanding.OpenUntil(at(17)), resolver.resolve(hours, DayOfWeek.MONDAY, at(15, 59)))
    }

    @Test
    fun `test the end of the interval is already closed`() {
        val hours = listOf(interval(DayOfWeek.MONDAY, "09:00", "17:00"))

        assertTrue(resolver.resolve(hours, DayOfWeek.MONDAY, at(16, 59)).isOpen)
        assertFalse(resolver.resolve(hours, DayOfWeek.MONDAY, at(17)).isOpen)
    }

    @Test
    fun `test before the opening the place opens today`() {
        val hours = listOf(interval(DayOfWeek.MONDAY, "09:00", "12:00"), interval(DayOfWeek.MONDAY, "14:00", "18:00"))

        assertEquals(PlaceStanding.OpensToday(at(9)), resolver.resolve(hours, DayOfWeek.MONDAY, at(7)))
        assertEquals(PlaceStanding.OpensToday(at(14)), resolver.resolve(hours, DayOfWeek.MONDAY, at(13)))
    }

    @Test
    fun `test after the last interval the next opening is the next day with hours`() {
        val hours = listOf(
            interval(DayOfWeek.MONDAY, "09:00", "12:00"),
            interval(DayOfWeek.WEDNESDAY, "10:00", "14:00"),
        )

        assertEquals(
            PlaceStanding.ClosedUntil(2, DayOfWeek.WEDNESDAY, at(10), at(14)),
            resolver.resolve(hours, DayOfWeek.MONDAY, at(15)),
        )
        assertEquals(
            PlaceStanding.ClosedUntil(5, DayOfWeek.MONDAY, at(9), at(12)),
            resolver.resolve(hours, DayOfWeek.WEDNESDAY, at(15)),
        )
    }

    @Test
    fun `test tomorrow is one day ahead`() {
        val hours = listOf(interval(DayOfWeek.TUESDAY, "09:00", "12:00"))

        val standing = resolver.resolve(hours, DayOfWeek.MONDAY, at(20)) as PlaceStanding.ClosedUntil

        assertEquals(1, standing.daysAhead)
    }

    @Test
    fun `test a place that works one day a week waits a full week after its day`() {
        val hours = listOf(interval(DayOfWeek.TUESDAY, "09:00", "12:00"))

        val standing = resolver.resolve(hours, DayOfWeek.TUESDAY, at(13)) as PlaceStanding.ClosedUntil

        assertEquals(7, standing.daysAhead)
        assertEquals(DayOfWeek.TUESDAY, standing.day)
    }
}
