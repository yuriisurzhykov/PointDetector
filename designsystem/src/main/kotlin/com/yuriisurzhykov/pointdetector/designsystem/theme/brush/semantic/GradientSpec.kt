package com.yuriisurzhykov.pointdetector.designsystem.theme.brush.semantic

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/** A gradient described as data, so a brush can be rebuilt for any size without the colours leaking out. */
@Stable
sealed interface GradientSpec {
    fun toBrush(): Brush
    fun toBrush(size: Size): Brush

    @Immutable
    data class Linear(
        val colors: List<Color>,
        val angle: Float = 0f,
    ) : GradientSpec {

        override fun toBrush(): Brush = Brush.linearGradient(colors = colors)

        override fun toBrush(size: Size): Brush {
            val rad = angle * PI.toFloat() / DEGREES_HALF_TURN
            val dx = cos(rad)
            val dy = sin(rad)
            val halfProjection = (abs(size.width * dx) + abs(size.height * dy)) / 2f
            val cx = size.width / 2f
            val cy = size.height / 2f
            return Brush.linearGradient(
                colors = colors,
                start = Offset(cx - dx * halfProjection, cy - dy * halfProjection),
                end = Offset(cx + dx * halfProjection, cy + dy * halfProjection),
            )
        }
    }

    @Immutable
    data class Radial(
        val colors: List<Color>,
        val center: Offset = Offset.Unspecified,
        val radius: Float = Float.POSITIVE_INFINITY,
    ) : GradientSpec {

        override fun toBrush(): Brush = Brush.radialGradient(
            colors = colors,
            center = center,
            radius = radius,
        )

        override fun toBrush(size: Size): Brush = Brush.radialGradient(
            colors = colors,
            center = if (center == Offset.Unspecified) Offset(size.width / 2f, size.height / 2f) else center,
            radius = if (radius == Float.POSITIVE_INFINITY) maxOf(size.width, size.height) / 2f else radius,
        )
    }

    @Immutable
    data class Solid(
        val color: Color,
    ) : GradientSpec {
        override fun toBrush(): Brush = SolidColor(color)
        override fun toBrush(size: Size): Brush = SolidColor(color)
    }

    private companion object {
        const val DEGREES_HALF_TURN = 180f
    }
}
