package com.yuriisurzhykov.pointdetector.uicomponents.model

import androidx.compose.runtime.Immutable

/** Bring the row with [key] into view; change [token] to ask for the same row again. */
@Immutable
data class PdScrollTo(val key: String, val token: Int = 0) : PdListScroll {
    override fun indexIn(keys: List<String>): Int = keys.indexOf(key)
}
