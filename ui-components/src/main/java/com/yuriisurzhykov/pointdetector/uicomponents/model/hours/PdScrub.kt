package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Stable

/**
 * Strategy of interaction with the axis. It replaces a nullable `onScrub` callback: a timeline
 * that cannot be dragged gets [PdScrubOff], not a null.
 */
@Stable
interface PdScrub {
    val enabled: Boolean

    /** [position] is a fraction of the axis, not a time. */
    fun onScrub(position: Float)
}
