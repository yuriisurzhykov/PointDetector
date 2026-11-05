package com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

/**
 * The nested scroll contract of the sheet: until it is fully open the sheet takes the drag, so
 * the list does not scroll; once open the list scrolls, and a drag down from the top of the list
 * goes back to the sheet. A fling is always settled to the nearest position.
 */
internal class PdSheetNestedScroll(private val controller: PdSheetController) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (controller.isFull) return Offset.Zero
        return Offset(0f, controller.drag(available.y))
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        if (available.y <= 0f) return Offset.Zero
        return Offset(0f, controller.drag(available.y))
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (controller.isFull) return Velocity.Zero
        controller.settle(available.y)
        return available
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        controller.settle(available.y)
        return available
    }
}
