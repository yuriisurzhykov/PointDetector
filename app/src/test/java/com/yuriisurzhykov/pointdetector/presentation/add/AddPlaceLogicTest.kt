package com.yuriisurzhykov.pointdetector.presentation.add

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.DayOfWeek
import java.util.Locale

class AddPlaceLogicTest {

    private val weekdays = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)

    // ---- the time fields ----

    @Test
    fun `test four digits are a time and anything else is not`() {
        assertEquals(9 * 60, TimeDigits.minutes("0900"))
        assertEquals(17 * 60 + 30, TimeDigits.minutes("1730"))
        assertEquals(24 * 60, TimeDigits.minutes("2400"))
        assertNull(TimeDigits.minutes("090"))
        assertNull(TimeDigits.minutes("2500"))
        assertNull(TimeDigits.minutes("0960"))
        assertNull(TimeDigits.minutes("2430"))
        assertNull(TimeDigits.minutes("09:0"))
    }

    @Test
    fun `test a minute is written as digits and as the clock of the data`() {
        assertEquals("0905", TimeDigits.digits(9 * 60 + 5))
        assertEquals("09:05", TimeDigits.clock(9 * 60 + 5))
    }

    // ---- the draft in the sheet ----

    @Test
    fun `test a draft without days asks for days`() {
        assertEquals(DraftProblem.NoDays, HoursDraft().problem)
    }

    @Test
    fun `test a wrong time is reported before the missing days`() {
        assertEquals(DraftProblem.BadTime, HoursDraft(fromDigits = "17", toDigits = "1700").problem)
        assertEquals(DraftProblem.BadTime, HoursDraft(days = weekdays, fromDigits = "1700", toDigits = "0900").problem)
        assertEquals(DraftProblem.BadTime, HoursDraft(days = weekdays, fromDigits = "0900", toDigits = "0900").problem)
    }

    @Test
    fun `test a complete draft makes a group`() {
        val group = HoursDraft(days = weekdays).toGroup()

        assertEquals(HoursGroup(weekdays, 9 * 60, 17 * 60), group)
    }

    @Test
    fun `test a draft with a problem makes no group`() {
        assertNull(HoursDraft().toGroup())
    }

    @Test
    fun `test a day is toggled on and off`() {
        val on = HoursDraft().toggled(DayOfWeek.SATURDAY)

        assertEquals(setOf(DayOfWeek.SATURDAY), on.days)
        assertEquals(emptySet<DayOfWeek>(), on.toggled(DayOfWeek.SATURDAY).days)
    }

    // ---- the groups ----

    @Test
    fun `test an overlapping day moves to the new group`() {
        val week = HoursGroup(weekdays, 9 * 60, 17 * 60)
        val saturdayAndFriday = HoursGroup(setOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY), 10 * 60, 14 * 60)

        val result = HoursGroups.add(listOf(week), saturdayAndFriday)

        assertEquals(
            listOf(
                HoursGroup(weekdays - DayOfWeek.FRIDAY, 9 * 60, 17 * 60),
                saturdayAndFriday,
            ),
            result,
        )
    }

    @Test
    fun `test a group that lost all its days disappears`() {
        val saturday = HoursGroup(setOf(DayOfWeek.SATURDAY), 9 * 60, 12 * 60)
        val replacement = HoursGroup(setOf(DayOfWeek.SATURDAY), 10 * 60, 13 * 60)

        assertEquals(listOf(replacement), HoursGroups.add(listOf(saturday), replacement))
    }

    @Test
    fun `test groups are kept in the order of the week`() {
        val sunday = HoursGroup(setOf(DayOfWeek.SUNDAY), 9 * 60, 12 * 60)
        val monday = HoursGroup(setOf(DayOfWeek.MONDAY), 9 * 60, 12 * 60)

        assertEquals(listOf(monday, sunday), HoursGroups.add(listOf(sunday), monday))
    }

    @Test
    fun `test a group is removed by its place in the list`() {
        val first = HoursGroup(setOf(DayOfWeek.MONDAY), 9 * 60, 12 * 60)
        val second = HoursGroup(setOf(DayOfWeek.TUESDAY), 9 * 60, 12 * 60)

        assertEquals(listOf(second), HoursGroups.remove(listOf(first, second), 0))
    }

    @Test
    fun `test groups become one entry per day in the form of the data`() {
        val days = HoursGroups.toWeekDays(
            listOf(HoursGroup(setOf(DayOfWeek.TUESDAY, DayOfWeek.MONDAY), 9 * 60, 17 * 60 + 30)),
        )

        assertEquals(listOf("Mon", "Tue"), days.map { it.dayName })
        assertEquals(listOf(1, 2), days.map { it.dayValue })
        assertEquals("09:00", days.first().hoursFrom)
        assertEquals("17:30", days.first().hoursTo)
    }

    @Test
    fun `test days are written as runs`() {
        assertEquals("Mon–Fri", HoursGroups.daysLabel(weekdays, Locale.ENGLISH))
        assertEquals("Mon, Tue", HoursGroups.daysLabel(setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), Locale.ENGLISH))
        assertEquals("Mon–Wed, Sat", HoursGroups.daysLabel(setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY), Locale.ENGLISH))
    }

    // ---- how to get help ----

    @Test
    fun `test an unanswered fact is not written`() {
        val help = FactAnswers.toHelp(
            mapOf("appointment" to FactAnswer.Yes, "documents" to FactAnswer.Unknown, "delivery" to FactAnswer.No),
        )

        assertEquals(mapOf("appointment" to true, "delivery" to false), help)
    }

    @Test
    fun `test no answers give no data`() {
        assertEquals(emptyMap<String, Boolean>(), FactAnswers.toHelp(emptyMap()))
        assertEquals(FactAnswer.Unknown, AddPlaceState().answer("appointment"))
    }
}
