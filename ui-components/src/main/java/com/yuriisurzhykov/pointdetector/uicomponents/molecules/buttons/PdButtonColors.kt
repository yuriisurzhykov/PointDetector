package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

@Immutable
internal data class PdButtonColors(
    val container: Color,
    val content: Color,
    val border: Color,
)

/** The brand fill: the one action a screen exists for. */
@Composable
@ReadOnlyComposable
internal fun filledButtonColors(): PdButtonColors {
    val primary = PdTheme.colors.primary
    return PdButtonColors(container = primary.main, content = primary.onMain, border = Color.Transparent)
}

/** A neutral, unfilled button: a secondary action next to a filled one. */
@Composable
@ReadOnlyComposable
internal fun outlinedButtonColors(): PdButtonColors {
    val content = PdTheme.colors.content
    return PdButtonColors(container = Color.Transparent, content = content.primary, border = content.secondary)
}
