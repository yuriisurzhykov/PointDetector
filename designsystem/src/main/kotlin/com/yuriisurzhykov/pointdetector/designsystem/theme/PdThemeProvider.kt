package com.yuriisurzhykov.pointdetector.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalHapticFeedback
import com.yuriisurzhykov.pointdetector.designsystem.theme.brush.PdBrushScheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.brush.PdDarkBrushScheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.brush.PdLightBrushScheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.PdColorScheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.PdDarkColorScheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.PdLightColorScheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.CompactDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.dimens.PdDimensions
import com.yuriisurzhykov.pointdetector.designsystem.theme.haptics.NoHaptics
import com.yuriisurzhykov.pointdetector.designsystem.theme.haptics.PdHaptics
import com.yuriisurzhykov.pointdetector.designsystem.theme.haptics.PlatformHaptics
import com.yuriisurzhykov.pointdetector.designsystem.theme.motion.DefaultMotion
import com.yuriisurzhykov.pointdetector.designsystem.theme.motion.PdMotion
import com.yuriisurzhykov.pointdetector.designsystem.theme.motion.ReducedMotion
import com.yuriisurzhykov.pointdetector.designsystem.theme.motion.rememberReduceMotion
import com.yuriisurzhykov.pointdetector.designsystem.theme.opacity.DefaultOpacity
import com.yuriisurzhykov.pointdetector.designsystem.theme.opacity.PdOpacity
import com.yuriisurzhykov.pointdetector.designsystem.theme.shapes.DefaultShapes
import com.yuriisurzhykov.pointdetector.designsystem.theme.shapes.PdShapes
import com.yuriisurzhykov.pointdetector.designsystem.theme.typography.PdTypography
import com.yuriisurzhykov.pointdetector.designsystem.theme.typography.buildTypography

@Composable
fun PdTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dimensions: PdDimensions = CompactDimensions,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) PdDarkColorScheme else PdLightColorScheme
    val brushes = if (darkTheme) PdDarkBrushScheme else PdLightBrushScheme
    val typography = remember { buildTypography() }
    val motion = if (rememberReduceMotion()) ReducedMotion else DefaultMotion
    val feedback = LocalHapticFeedback.current
    val haptics = remember(feedback) { PlatformHaptics(feedback) }

    CompositionLocalProvider(
        LocalPdColors provides colors,
        LocalPdShapes provides DefaultShapes,
        LocalPdTypography provides typography,
        LocalPdBrushes provides brushes,
        LocalPdDimensions provides dimensions,
        LocalPdMotion provides motion,
        LocalPdOpacity provides DefaultOpacity,
        LocalPdHaptics provides haptics,
        content = content,
    )
}

val LocalPdColors = staticCompositionLocalOf<PdColorScheme> { PdLightColorScheme }
val LocalPdShapes = staticCompositionLocalOf<PdShapes> { DefaultShapes }
val LocalPdTypography = staticCompositionLocalOf<PdTypography> { buildTypography() }
val LocalPdBrushes = staticCompositionLocalOf<PdBrushScheme> { PdLightBrushScheme }
val LocalPdDimensions = staticCompositionLocalOf<PdDimensions> { CompactDimensions }
val LocalPdMotion = staticCompositionLocalOf<PdMotion> { DefaultMotion }
val LocalPdOpacity = staticCompositionLocalOf<PdOpacity> { DefaultOpacity }
val LocalPdHaptics = staticCompositionLocalOf<PdHaptics> { NoHaptics }
