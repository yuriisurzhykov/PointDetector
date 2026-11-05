package com.yuriisurzhykov.pointdetector.designsystem.atoms.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.color

private const val FULL_TURN = 360f
private const val ARC_SWEEP = 270f

/** A rotating arc. Static under "reduce motion", so a loading state is still visible. */
@Immutable
class PdArcSpinner(
    private val tone: PdContentTone = PdContentTone.Inherit,
) : PdBaseComponent(), PdSpinner {

    @Composable
    override fun Render(modifier: Modifier) {
        val dimensions = PdTheme.dimensions.component
        val motion = PdTheme.motion
        val color = tone.color()
        val animated by rememberInfiniteTransition(label = "spinner").animateFloat(
            initialValue = 0f,
            targetValue = FULL_TURN,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = motion.spinMillis.coerceAtLeast(1), easing = LinearEasing),
            ),
            label = "angle",
        )
        val angle = if (motion.isReduced) 0f else animated
        Canvas(modifier.size(dimensions.spinnerSize)) {
            val stroke = dimensions.spinnerStroke.toPx()
            drawArc(
                color = color,
                startAngle = angle,
                sweepAngle = ARC_SWEEP,
                useCenter = false,
                topLeft = Offset(stroke / 2f, stroke / 2f),
                size = Size(size.width - stroke, size.height - stroke),
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
        }
    }
}
