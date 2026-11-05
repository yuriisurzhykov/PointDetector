package com.yuriisurzhykov.pointdetector.designsystem.theme.brush

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.designsystem.theme.brush.semantic.GradientSpec

@Immutable
data class PdBrushScheme(
    val actionGradient: GradientSpec,
    val highlightGradient: GradientSpec,
)
