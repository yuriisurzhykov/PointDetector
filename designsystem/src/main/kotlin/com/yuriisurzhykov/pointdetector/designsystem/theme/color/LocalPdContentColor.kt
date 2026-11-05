package com.yuriisurzhykov.pointdetector.designsystem.theme.color

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * The colour that text and icons without an explicit tone inherit. A button provides its own
 * content colour here, so the label and the icon inside always match the button.
 */
val LocalPdContentColor = compositionLocalOf { Color.Unspecified }
