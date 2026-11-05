package com.yuriisurzhykov.pointdetector.uicomponents

import androidx.compose.ui.text.AnnotatedString
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdChoice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdSegmentedSteps
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdChoiceSegments
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdTimeMask
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdClosingSoon
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.help.PdHelpList
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.hours.PdWeekSchedule
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.rows.PdBorderedRows
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ComponentContractsTest {

    private val text = PdLiteralText("x")

    @Test
    fun `test four digits are shown as a time`() {
        assertEquals("09:00", PdTimeMask.filter(AnnotatedString("0900")).text.text)
        assertEquals("09:3", PdTimeMask.filter(AnnotatedString("093")).text.text)
        assertEquals("09", PdTimeMask.filter(AnnotatedString("09")).text.text)
        assertEquals("", PdTimeMask.filter(AnnotatedString("")).text.text)
    }

    @Test
    fun `test the time mask maps the cursor across the colon`() {
        val mapping = PdTimeMask.filter(AnnotatedString("0900")).offsetMapping

        assertEquals(2, mapping.originalToTransformed(2))
        assertEquals(4, mapping.originalToTransformed(3))
        assertEquals(5, mapping.originalToTransformed(4))
        assertEquals(2, mapping.transformedToOriginal(2))
        assertEquals(3, mapping.transformedToOriginal(4))
    }

    @Test
    fun `test a step must be one of the steps`() {
        PdSegmentedSteps(step = 3, total = 3, label = text)

        assertThrows(IllegalArgumentException::class.java) { PdSegmentedSteps(0, 3, text) }
        assertThrows(IllegalArgumentException::class.java) { PdSegmentedSteps(4, 3, text) }
        assertThrows(IllegalArgumentException::class.java) { PdSegmentedSteps(1, 0, text) }
    }

    @Test
    fun `test groups and lists that would be empty are refused`() {
        assertThrows(IllegalArgumentException::class.java) { PdChoiceSegments(listOf(PdChoice(text, true) {})) }
        assertThrows(IllegalArgumentException::class.java) { PdBorderedRows(emptyList()) }
        assertThrows(IllegalArgumentException::class.java) { PdWeekSchedule(emptyList()) }
        assertThrows(IllegalArgumentException::class.java) { PdHelpList(text, emptyList()) }
    }

    @Test
    fun `test negative minutes left are refused`() {
        PdClosingSoon(minutesLeft = 0)

        assertThrows(IllegalArgumentException::class.java) { PdClosingSoon(minutesLeft = -1) }
    }
}
