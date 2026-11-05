package com.yuriisurzhykov.pointdetector.uicomponents.atoms.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** A placeholder bar with a shimmer sweep. Under "reduce motion" the sweep is not drawn. */
@Immutable
class PdSkeletonBlock(
    private val size: PdSkeletonSize = PdSkeletonSize.Line,
    private val width: PdSkeletonWidth = PdSkeletonWidth.Full,
) : PdBaseComponent(), PdSkeleton {

    @Composable
    override fun Render(modifier: Modifier) {
        val dimensions = PdTheme.dimensions.component
        val motion = PdTheme.motion
        val colors = PdTheme.colors
        val height = when (size) {
            PdSkeletonSize.Line -> dimensions.skeletonLineHeight
            PdSkeletonSize.Title -> dimensions.skeletonTitleHeight
            PdSkeletonSize.Chip -> dimensions.skeletonChipHeight
        }
        val fraction = when (width) {
            PdSkeletonWidth.Full -> 1f
            PdSkeletonWidth.Wide -> dimensions.skeletonWideFraction
            PdSkeletonWidth.Narrow -> dimensions.skeletonNarrowFraction
        }
        val sweep by rememberInfiniteTransition(label = "skeleton").animateFloat(
            initialValue = -1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = motion.shimmerMillis.coerceAtLeast(1), easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "sweep",
        )
        val highlight = colors.divider
        Box(
            modifier = modifier
                .fillMaxWidth(fraction)
                .height(height)
                .clip(PdTheme.shapes.small)
                .background(colors.surface.container)
                .drawWithContent {
                    drawContent()
                    if (!motion.isReduced) {
                        val start = sweep * size.width
                        drawRect(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, highlight, Color.Transparent),
                                startX = start,
                                endX = start + size.width,
                            ),
                        )
                    }
                },
        )
    }
}
