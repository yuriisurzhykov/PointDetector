package com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet

import kotlin.math.abs
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.semantic.ComponentDimensions
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState

/** The visible heights (in pixels) at which a sheet of a screen this high can rest. */
internal class PdSheetStops(screenHeight: Float, component: ComponentDimensions) {
    val min = screenHeight * component.sheetMinFraction
    val full = screenHeight * component.sheetFullFraction
    private val peek = screenHeight * component.sheetPeekFraction
    private val half = screenHeight * component.sheetHalfFraction
    private val floatShow = screenHeight * component.sheetFloatShowFraction
    private val floatHide = screenHeight * component.sheetFloatHideFraction

    fun visible(state: PdSheetState): Float = when (state) {
        PdSheetState.Peek -> peek
        PdSheetState.Half -> half
        PdSheetState.Full -> full
    }

    fun nearest(position: Float): PdSheetState = PdSheetState.entries.minBy { abs(visible(it) - position) }

    /** 1 while the sheet is low, fading to 0 as it nears the top, so a floating button never covers the list. */
    fun floatingAlpha(visibleHeight: Float): Float =
        ((floatHide - visibleHeight) / (floatHide - floatShow)).coerceIn(0f, 1f)
}
