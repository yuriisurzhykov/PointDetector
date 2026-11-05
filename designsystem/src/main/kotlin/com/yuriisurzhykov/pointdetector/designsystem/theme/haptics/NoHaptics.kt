package com.yuriisurzhykov.pointdetector.designsystem.theme.haptics

/** Default outside a theme: does nothing. */
object NoHaptics : PdHaptics {
    override fun perform(effect: PdHapticEffect) = Unit
}
