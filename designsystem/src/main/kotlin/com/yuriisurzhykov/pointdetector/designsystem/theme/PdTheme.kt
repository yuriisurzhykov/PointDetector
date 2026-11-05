package com.yuriisurzhykov.pointdetector.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.yuriisurzhykov.pointdetector.designsystem.theme.brush.PdBrushScheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.PdColorScheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.PdDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.haptics.PdHaptics
import com.yuriisurzhykov.pointdetector.designsystem.theme.motion.PdMotion
import com.yuriisurzhykov.pointdetector.designsystem.theme.opacity.PdOpacity
import com.yuriisurzhykov.pointdetector.designsystem.theme.shapes.PdShapes
import com.yuriisurzhykov.pointdetector.designsystem.theme.typography.PdTypography

/** The single entry point to every token: `PdTheme.colors`, `PdTheme.dimensions`, ... */
object PdTheme {
    val colors: PdColorScheme
        @Composable @ReadOnlyComposable
        get() = LocalPdColors.current

    val shapes: PdShapes
        @Composable @ReadOnlyComposable
        get() = LocalPdShapes.current

    val typography: PdTypography
        @Composable @ReadOnlyComposable
        get() = LocalPdTypography.current

    val brushes: PdBrushScheme
        @Composable @ReadOnlyComposable
        get() = LocalPdBrushes.current

    val dimensions: PdDimensions
        @Composable @ReadOnlyComposable
        get() = LocalPdDimensions.current

    val motion: PdMotion
        @Composable @ReadOnlyComposable
        get() = LocalPdMotion.current

    val opacity: PdOpacity
        @Composable @ReadOnlyComposable
        get() = LocalPdOpacity.current

    val haptics: PdHaptics
        @Composable @ReadOnlyComposable
        get() = LocalPdHaptics.current
}
