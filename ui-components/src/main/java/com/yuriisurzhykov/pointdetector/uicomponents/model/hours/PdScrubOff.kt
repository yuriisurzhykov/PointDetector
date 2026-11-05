package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Immutable

/** The axis is read only. */
@Immutable
object PdScrubOff : PdScrub {
    override val enabled: Boolean = false
    override fun onScrub(position: Float) = Unit
}
