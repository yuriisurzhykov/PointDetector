package com.yuriisurzhykov.pointdetector.designsystem.theme.haptics

import androidx.compose.runtime.Stable
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

/** Maps the project's effects to what the device offers. */
@Stable
class PlatformHaptics(private val feedback: HapticFeedback) : PdHaptics {

    override fun perform(effect: PdHapticEffect) {
        feedback.performHapticFeedback(effect.toPlatform())
    }

    private fun PdHapticEffect.toPlatform(): HapticFeedbackType = when (this) {
        PdHapticEffect.SegmentTick -> HapticFeedbackType.SegmentTick
        PdHapticEffect.Confirm -> HapticFeedbackType.Confirm
        PdHapticEffect.ToggleOn -> HapticFeedbackType.ToggleOn
        PdHapticEffect.ToggleOff -> HapticFeedbackType.ToggleOff
        PdHapticEffect.Reject -> HapticFeedbackType.Reject
    }
}
