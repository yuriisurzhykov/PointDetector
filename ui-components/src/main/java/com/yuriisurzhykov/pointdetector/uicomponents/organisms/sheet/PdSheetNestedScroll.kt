package com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

/**
 * The nested scroll contract of the sheet: **the list always scrolls first**, in every position
 * of the sheet, so the cards can be browsed while the sheet is low. The sheet takes only what the
 * list cannot use: a drag down from the top of the list closes the sheet. The sheet is opened by
 * its handle and header, which are draggable too. A fling settles the sheet only when the sheet
 * has moved during that gesture, so a fling of the list alone never changes the position.
 */
internal class PdSheetNestedScroll(private val controller: PdSheetController) : NestedScrollConnection {

    private var moved = false

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        if (available.y <= 0f) return Offset.Zero
        val used = controller.drag(available.y)
        if (used != 0f) moved = true
        return Offset(0f, used)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (!moved) return Velocity.Zero
        moved = false
        controller.settle(available.y)
        return available
    }
}
