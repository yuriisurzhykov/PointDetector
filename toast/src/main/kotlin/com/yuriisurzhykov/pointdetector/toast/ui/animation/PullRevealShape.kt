package com.yuriisurzhykov.pointdetector.toast.ui.animation

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.yuriisurzhykov.pointdetector.toast.tokens.ToastTokens

/**
 * Clip producing a "pull-reveal" from the top edge: the bottom boundary is a cubic Bezier
 * whose centre drops ahead of the corners. When [centerProgress] equals [edgeProgress] it is
 * a plain rectangle.
 */
@Stable
class PullRevealShape(
    private val centerProgress: Float,
    private val edgeProgress: Float,
) : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val width = size.width
        val height = size.height
        val centerDrop = height * centerProgress
        val edgeDrop = height * edgeProgress

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width, edgeDrop)
            cubicTo(
                x1 = width * ToastTokens.BEZIER_NEAR, y1 = centerDrop,
                x2 = width * ToastTokens.BEZIER_FAR, y2 = centerDrop,
                x3 = 0f, y3 = edgeDrop,
            )
            close()
        }
        return Outline.Generic(path)
    }

    override fun equals(other: Any?): Boolean =
        other is PullRevealShape && centerProgress == other.centerProgress && edgeProgress == other.edgeProgress

    override fun hashCode(): Int = 31 * centerProgress.hashCode() + edgeProgress.hashCode()
}
