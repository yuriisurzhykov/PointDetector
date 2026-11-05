package com.yuriisurzhykov.pointdetector.uicomponents.model

import androidx.compose.runtime.Immutable

/** Without data: do not scroll. */
@Immutable
object PdNoScroll : PdListScroll {
    override fun indexIn(keys: List<String>): Int = -1
}
