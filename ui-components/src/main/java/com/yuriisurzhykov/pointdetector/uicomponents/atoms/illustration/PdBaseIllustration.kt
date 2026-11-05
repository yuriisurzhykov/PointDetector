package com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver

private const val RING_COUNT = 2

/**
 * The shared drawing of an illustration. The subclasses choose the colours and the size. Under
 * "reduce motion" the rings are not drawn.
 */
@Immutable
abstract class PdBaseIllustration(
    private val icon: PdDrawableResolver,
    private val large: Boolean,
) : PdBaseComponent(), PdIllustration {

    @Composable
    internal abstract fun disc(): Color

    @Composable
    internal abstract fun glyph(): Color

    @Composable
    final override fun Render(modifier: Modifier) {
        val component = PdTheme.dimensions.component
        val motion = PdTheme.motion
        val colors = PdTheme.colors
        val disc = disc()
        val size: Dp = if (large) component.illustrationLarge else component.illustrationSmall
        val glyphSize: Dp = if (large) component.illustrationLargeIcon else component.illustrationSmallIcon
        val ringColor = colors.primary.main
        val ringOpacity = PdTheme.opacity.ring
        val transition = rememberInfiniteTransition(label = "illustration")
        val progress = transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(motion.pulseMillis.coerceAtLeast(1), easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "rings",
        )
        Box(modifier.size(size), contentAlignment = Alignment.Center) {
            if (!motion.isReduced) {
                repeat(RING_COUNT) { index ->
                    Box(
                        Modifier
                            .size(size)
                            .graphicsLayer {
                                val phase = (progress.value + index.toFloat() / RING_COUNT) % 1f
                                val grow = 1f + (component.illustrationRingScale - 1f) * phase
                                scaleX = grow
                                scaleY = grow
                                alpha = ringOpacity * (1f - phase)
                            }
                            .border(PdTheme.dimensions.stroke.thick, ringColor, PdTheme.shapes.full),
                    )
                }
            }
            Box(
                Modifier
                    .size(size)
                    .background(disc, PdTheme.shapes.full),
                contentAlignment = Alignment.Center,
            ) {
                PdTintedImage(icon, glyph(), glyphSize)
            }
        }
    }
}
