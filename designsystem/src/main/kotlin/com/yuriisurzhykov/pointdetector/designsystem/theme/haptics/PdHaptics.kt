package com.yuriisurzhykov.pointdetector.designsystem.theme.haptics

import androidx.compose.runtime.Stable

/** The only way a component asks the device to vibrate. Swapped for a recorder in tests. */
@Stable
interface PdHaptics {
    fun perform(effect: PdHapticEffect)
}
