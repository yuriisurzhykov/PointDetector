package com.yuriisurzhykov.pointdetector.designsystem.theme.brush

import com.yuriisurzhykov.pointdetector.designsystem.theme.brush.semantic.GradientSpec
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.Palette

private const val ACTION_ANGLE = 135f

val PdLightBrushScheme = PdBrushScheme(
    actionGradient = GradientSpec.Linear(listOf(Palette.Blue500, Palette.Blue300), ACTION_ANGLE),
    highlightGradient = GradientSpec.Linear(listOf(Palette.Blue100, Palette.Blue50), ACTION_ANGLE),
)

val PdDarkBrushScheme = PdBrushScheme(
    actionGradient = GradientSpec.Linear(listOf(Palette.Blue300, Palette.Blue400), ACTION_ANGLE),
    highlightGradient = GradientSpec.Linear(listOf(Palette.Blue800, Palette.Blue850), ACTION_ANGLE),
)
