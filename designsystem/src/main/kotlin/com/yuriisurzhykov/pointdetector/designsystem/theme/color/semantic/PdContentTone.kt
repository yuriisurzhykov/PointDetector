package com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.LocalPdContentColor

/**
 * Colour of text and icons as a role, not a value. Components take a tone in their API,
 * so a raw `Color` never appears in a signature.
 */
enum class PdContentTone {
    Inherit,
    Primary,
    Secondary,
    Tertiary,
    Brand,
    Inverse,
}

@Composable
@ReadOnlyComposable
fun PdContentTone.color(): Color = when (this) {
    PdContentTone.Inherit -> LocalPdContentColor.current.takeOrElse { PdTheme.colors.content.primary }
    PdContentTone.Primary -> PdTheme.colors.content.primary
    PdContentTone.Secondary -> PdTheme.colors.content.secondary
    PdContentTone.Tertiary -> PdTheme.colors.content.tertiary
    PdContentTone.Brand -> PdTheme.colors.primary.main
    PdContentTone.Inverse -> PdTheme.colors.content.inverse
}
