package com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** Typographic role of a text. One-to-one with the scale in [PdTheme.typography]. */
enum class PdTextRole {
    Display,
    HeadingLarge,
    HeadingMedium,
    HeadingSmall,
    TitleLarge,
    TitleMedium,
    TitleSmall,
    BodyLarge,
    BodyMedium,
    BodySmall,
    LabelLarge,
    LabelMedium,
    LabelSmall,
}

@Composable
@ReadOnlyComposable
internal fun PdTextRole.style(): TextStyle {
    val typography = PdTheme.typography
    return when (this) {
        PdTextRole.Display -> typography.displayLarge
        PdTextRole.HeadingLarge -> typography.headingLarge
        PdTextRole.HeadingMedium -> typography.headingMedium
        PdTextRole.HeadingSmall -> typography.headingSmall
        PdTextRole.TitleLarge -> typography.titleLarge
        PdTextRole.TitleMedium -> typography.titleMedium
        PdTextRole.TitleSmall -> typography.titleSmall
        PdTextRole.BodyLarge -> typography.bodyLarge
        PdTextRole.BodyMedium -> typography.bodyMedium
        PdTextRole.BodySmall -> typography.bodySmall
        PdTextRole.LabelLarge -> typography.labelLarge
        PdTextRole.LabelMedium -> typography.labelMedium
        PdTextRole.LabelSmall -> typography.labelSmall
    }
}
