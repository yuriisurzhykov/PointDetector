package com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet

import androidx.compose.animation.core.spring
import androidx.compose.runtime.mutableFloatStateOf
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.CompactDimensions
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SheetLogicTest {

    private val screen = 1_000f
    private val stops = PdSheetStops(screen, CompactDimensions.component)

    @Test
    fun `test resting positions grow from peek to full`() {
        val peek = stops.visible(PdSheetState.Peek)
        val half = stops.visible(PdSheetState.Half)
        val full = stops.visible(PdSheetState.Full)

        assertTrue(stops.min < peek)
        assertTrue(peek < half)
        assertTrue(half < full)
        assertTrue(full < screen)
    }

    @Test
    fun `test the nearest position is chosen for a projected height`() {
        assertEquals(PdSheetState.Peek, stops.nearest(0f))
        assertEquals(PdSheetState.Half, stops.nearest(stops.visible(PdSheetState.Half) + 10f))
        assertEquals(PdSheetState.Full, stops.nearest(screen * 2))
    }

    @Test
    fun `test the floating button is shown low and gone at the top`() {
        assertEquals(1f, stops.floatingAlpha(stops.visible(PdSheetState.Peek)), 0f)
        assertEquals(0f, stops.floatingAlpha(stops.visible(PdSheetState.Full)), 0f)
        val between = stops.floatingAlpha(stops.visible(PdSheetState.Half))
        assertTrue(between in 0f..1f)
    }

    @Test
    fun `test a drag is clamped between the minimum and the full height`() {
        val visible = mutableFloatStateOf(stops.visible(PdSheetState.Peek))
        val controller = controller(visible)

        val usedUp = controller.drag(-10_000f)
        assertEquals(stops.full, visible.floatValue, 0f)
        assertTrue(controller.isFull)
        assertTrue(usedUp < 0f)

        controller.drag(10_000f)
        assertEquals(stops.min, visible.floatValue, 0f)
        assertFalse(controller.isFull)
    }

    @Test
    fun `test a drag reports only the part the sheet used`() {
        val visible = mutableFloatStateOf(stops.full - 5f)
        val controller = controller(visible)

        assertEquals(-5f, controller.drag(-50f), 0.001f)
    }

    @Test
    fun `test a fast upward fling opens the sheet and a slow release keeps it`() {
        val changes = mutableListOf<PdSheetState>()
        val visible = mutableFloatStateOf(stops.visible(PdSheetState.Peek))
        val controller = controller(visible) { changes.add(it) }

        controller.settle(velocity = -3_000f)
        controller.settle(velocity = 0f)

        assertEquals(listOf(PdSheetState.Full, PdSheetState.Peek), changes)
    }

    // ---- the list scrolls in every position ----

    @Test
    fun `test the list is never stopped by the sheet before it scrolls`() {
        val visible = mutableFloatStateOf(stops.visible(PdSheetState.Peek))
        val nested = PdSheetNestedScroll(controller(visible))

        // The default of a connection is to leave the drag to the list: nothing is taken before it.
        assertEquals(Offset.Zero, nested.onPreScroll(Offset(0f, -40f), NestedScrollSource.UserInput))
        assertEquals(Offset.Zero, nested.onPreScroll(Offset(0f, 40f), NestedScrollSource.UserInput))
        assertEquals(stops.visible(PdSheetState.Peek), visible.floatValue, 0f)
    }

    @Test
    fun `test a drag down that the list cannot use closes the sheet`() {
        val visible = mutableFloatStateOf(stops.visible(PdSheetState.Half))
        val nested = PdSheetNestedScroll(controller(visible))

        val taken = nested.onPostScroll(Offset.Zero, Offset(0f, 30f), NestedScrollSource.UserInput)

        assertEquals(30f, taken.y, 0.001f)
        assertEquals(stops.visible(PdSheetState.Half) - 30f, visible.floatValue, 0.001f)
    }

    @Test
    fun `test a drag up that the list cannot use leaves the sheet where it is`() {
        val visible = mutableFloatStateOf(stops.visible(PdSheetState.Peek))
        val nested = PdSheetNestedScroll(controller(visible))

        nested.onPostScroll(Offset.Zero, Offset(0f, -30f), NestedScrollSource.UserInput)

        assertEquals(stops.visible(PdSheetState.Peek), visible.floatValue, 0f)
    }

    @Test
    fun `test a fling of the list alone never changes the position of the sheet`() = runBlocking {
        val changes = mutableListOf<PdSheetState>()
        val visible = mutableFloatStateOf(stops.visible(PdSheetState.Peek))
        val nested = PdSheetNestedScroll(controller(visible) { changes.add(it) })

        nested.onPostFling(Velocity.Zero, Velocity(0f, -4_000f))

        assertTrue(changes.isEmpty())
    }

    @Test
    fun `test a fling after the sheet was moved settles it once`() = runBlocking {
        val changes = mutableListOf<PdSheetState>()
        val visible = mutableFloatStateOf(stops.visible(PdSheetState.Half))
        val nested = PdSheetNestedScroll(controller(visible) { changes.add(it) })

        nested.onPostScroll(Offset.Zero, Offset(0f, 200f), NestedScrollSource.UserInput)
        nested.onPostFling(Velocity.Zero, Velocity(0f, 0f))
        nested.onPostFling(Velocity.Zero, Velocity(0f, 0f))

        assertEquals(1, changes.size)
    }

    private fun controller(
        visible: androidx.compose.runtime.MutableFloatState,
        onChange: (PdSheetState) -> Unit = {},
    ) = PdSheetController(
        scope = TestScope(),
        visible = visible,
        stops = stops,
        spec = spring(),
        projectionSeconds = 0.26f,
        onStateChange = onChange,
    )
}
