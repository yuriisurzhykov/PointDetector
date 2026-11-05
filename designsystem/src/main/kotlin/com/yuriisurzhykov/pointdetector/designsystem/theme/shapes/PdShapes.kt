package com.yuriisurzhykov.pointdetector.designsystem.theme.shapes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class PdShapes(
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val full: Shape,
    /** Rounded only at the top, for a sheet that rises from the bottom edge. */
    val sheet: Shape,
)

val DefaultShapes = PdShapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    full = RoundedCornerShape(percent = 50),
    sheet = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
)
