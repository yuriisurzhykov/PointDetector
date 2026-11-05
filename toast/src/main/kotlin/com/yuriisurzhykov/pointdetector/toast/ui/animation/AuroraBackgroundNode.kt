package com.yuriisurzhykov.pointdetector.toast.ui.animation

import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.ColorRole
import com.yuriisurzhykov.pointdetector.toast.tokens.ToastTokens
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val TWO_PI = (2.0 * PI).toFloat()
private const val NANOS_PER_MS = 1_000_000L
private const val HALF_TURN_DEG = 180f

/**
 * Renders the aurora and drives its own frame-synchronised loop. Phases come from absolute
 * frame time with modular arithmetic, so the motion is seamless across attach / detach and
 * never drifts.
 */
internal class AuroraBackgroundNode(
    private var role: ColorRole,
    private var animated: Boolean,
) : Modifier.Node(), DrawModifierNode {

    private var phaseA = 0f
    private var phaseB = 0f
    private var phaseC = 0f

    fun update(newRole: ColorRole, newAnimated: Boolean) {
        if (role != newRole || animated != newAnimated) {
            role = newRole
            animated = newAnimated
            invalidateDraw()
        }
    }

    override fun onAttach() {
        if (!animated) return
        coroutineScope.launch {
            while (true) {
                withFrameNanos { nanos ->
                    val ms = nanos / NANOS_PER_MS
                    phaseA = (ms % ToastTokens.PHASE_A_MS).toFloat() / ToastTokens.PHASE_A_MS * TWO_PI
                    phaseB = (ms % ToastTokens.PHASE_B_MS).toFloat() / ToastTokens.PHASE_B_MS * TWO_PI
                    phaseC = (ms % ToastTokens.PHASE_C_MS).toFloat() / ToastTokens.PHASE_C_MS * TWO_PI
                    invalidateDraw()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        val width = size.width
        val height = size.height
        val diagonal = sqrt(width * width + height * height)
        val glow = lerp(role.main, role.container, ToastTokens.GLOW_MIX)

        val angle = (ToastTokens.BASE_ANGLE_DEG + ToastTokens.BASE_ANGLE_SWING_DEG * sin(phaseA)) * PI.toFloat() / HALF_TURN_DEG
        val half = diagonal / 2f
        val centerX = width / 2f
        val centerY = height / 2f
        drawRect(
            Brush.linearGradient(
                colors = listOf(role.main, glow, role.container),
                start = Offset(centerX - cos(angle) * half, centerY - sin(angle) * half),
                end = Offset(centerX + cos(angle) * half, centerY + sin(angle) * half),
            ),
        )

        drawRect(
            Brush.radialGradient(
                colors = listOf(role.main.copy(alpha = ToastTokens.FOCAL_ALPHA), Color.Transparent),
                center = Offset(
                    x = width * (ToastTokens.FOCAL_X + ToastTokens.FOCAL_X_SWING * sin(phaseB)),
                    y = height * (ToastTokens.FOCAL_Y + ToastTokens.FOCAL_Y_SWING * cos(phaseB)),
                ),
                radius = diagonal * ToastTokens.FOCAL_RADIUS,
            ),
        )

        drawRect(
            Brush.radialGradient(
                colors = listOf(role.onContainer.copy(alpha = ToastTokens.ACCENT_ALPHA), Color.Transparent),
                center = Offset(
                    x = width * (ToastTokens.ACCENT_X + ToastTokens.ACCENT_X_SWING * cos(phaseC)),
                    y = height * (ToastTokens.ACCENT_Y + ToastTokens.ACCENT_Y_SWING * sin(phaseC)),
                ),
                radius = diagonal * ToastTokens.ACCENT_RADIUS,
            ),
        )

        drawContent()
    }
}
