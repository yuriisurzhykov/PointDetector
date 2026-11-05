package com.yuriisurzhykov.pointdetector.uicomponents.model

import androidx.compose.runtime.Stable

/**
 * An order for a list: which row to bring into view. A strategy instead of a nullable key:
 * [PdNoScroll] leaves the list alone.
 */
@Stable
interface PdListScroll {
    /** The index of the row to show among rows with these [keys], or -1 when there is none. */
    fun indexIn(keys: List<String>): Int
}
