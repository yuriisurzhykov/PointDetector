package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class HoursModelTest {

    @Test
    fun `test segment keeps the warning tail inside of the segment`() {
        val segment = PdSegment(start = 0.4f, end = 0.7f, warnFrom = 0.65f)

        assertEquals(0.65f, segment.warnFrom, 0f)
    }

    @Test
    fun `test segment without a warning tail warns from its end`() {
        assertEquals(0.7f, PdSegment(0.4f, 0.7f).warnFrom, 0f)
    }

    @Test
    fun `test segment rejects fractions outside of the axis or in the wrong order`() {
        assertThrows(IllegalArgumentException::class.java) { PdSegment(-0.1f, 0.5f) }
        assertThrows(IllegalArgumentException::class.java) { PdSegment(0.2f, 1.1f) }
        assertThrows(IllegalArgumentException::class.java) { PdSegment(0.6f, 0.4f) }
        assertThrows(IllegalArgumentException::class.java) { PdSegment(0.2f, 0.6f, warnFrom = 0.7f) }
        assertThrows(IllegalArgumentException::class.java) { PdSegment(0.2f, 0.6f, warnFrom = 0.1f) }
    }

    @Test
    fun `test axis mark rejects a position outside of the axis`() {
        assertThrows(IllegalArgumentException::class.java) { PdAxisMark(1.2f, PdLiteralText("x")) }
    }

    @Test
    fun `test scrub off does nothing and scrub to forwards the position`() {
        val received = mutableListOf<Float>()
        val to = PdScrubTo { received.add(it) }

        PdScrubOff.onScrub(0.5f)
        to.onScrub(0.25f)

        assertFalse(PdScrubOff.enabled)
        assertTrue(to.enabled)
        assertEquals(listOf(0.25f), received)
    }

    @Test
    fun `test day variants expose their segments`() {
        val withBreak = PdOpenWithBreak(PdSegment(0.3f, 0.5f), PdSegment(0.6f, 0.8f), PdLiteralText("x"))

        assertEquals(emptyList<PdSegment>(), PdDayOff.segments)
        assertEquals(listOf(PdSegment(0f, 1f)), PdOpenAllDay.segments)
        assertEquals(2, withBreak.segments.size)
    }
}
