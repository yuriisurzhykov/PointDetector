package com.yuriisurzhykov.pointdetector.designsystem.atoms.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

enum class PdIconSize {
    Small,
    Regular,
    Large,
}

@Composable
@ReadOnlyComposable
internal fun PdIconSize.dp(): Dp {
    val icons = PdTheme.dimensions.icon
    return when (this) {
        PdIconSize.Small -> icons.small
        PdIconSize.Regular -> icons.regular
        PdIconSize.Large -> icons.large
    }
}
