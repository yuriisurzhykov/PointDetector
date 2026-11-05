package com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * One accent in four tones. [main] fills solid shapes, [onMain] sits on it,
 * [container] is the soft tinted background and [onContainer] is the text on that tint.
 * The same shape serves brand roles and status roles, so a component never cares which it got.
 */
@Immutable
data class ColorRole(
    val main: Color,
    val onMain: Color,
    val container: Color,
    val onContainer: Color,
)
