package com.yuriisurzhykov.pointdetector.uicomponents.molecules.toggles

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.dp
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.haptics.PdHapticEffect
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val PARTICLES = 6
private const val FULL_FADE = 1f

/**
 * A heart that springs and throws a ring of particles when it becomes checked. The pop, the
 * particles and the vibration all come from theme tokens, and under "reduce motion" only the
 * fill changes.
 */
@Immutable
class PdHeartToggle(
    private val checked: Boolean,
    private val description: PdTextResolver,
    private val onCheckedChange: (Boolean) -> Unit,
) : PdBaseComponent(), PdFavoriteToggle {

    @Composable
    override fun Render(modifier: Modifier) {
        val motion = PdTheme.motion
        val haptics = PdTheme.haptics
        val component = PdTheme.dimensions.component
        val label = description.resolve().toString()
        val brand = PdTheme.colors.primary.main
        val progress = remember { Animatable(FULL_FADE) }
        var settled by remember { mutableStateOf(false) }

        LaunchedEffect(checked) {
            if (!settled) {
                settled = true
                return@LaunchedEffect
            }
            if (checked) {
                progress.snapTo(0f)
                progress.animateTo(FULL_FADE, motion.effects())
            }
        }

        Box(
            modifier = modifier
                .size(component.touchTarget)
                .toggleable(
                    value = checked,
                    role = Role.Checkbox,
                    onValueChange = { next ->
                        haptics.perform(if (next) PdHapticEffect.Confirm else PdHapticEffect.ToggleOff)
                        onCheckedChange(next)
                    },
                )
                .semantics { contentDescription = label }
                .drawBehind {
                    val fade = FULL_FADE - progress.value
                    if (!motion.isReduced && fade > 0f) {
                        val radius = component.burstParticleSize.toPx() / 2f
                        val distance = component.burstDistance.toPx() * progress.value
                        repeat(PARTICLES) { index ->
                            val angle = (2 * PI * index / PARTICLES).toFloat()
                            drawCircle(
                                color = brand.copy(alpha = fade),
                                radius = radius,
                                center = center + Offset(cos(angle) * distance, sin(angle) * distance),
                            )
                        }
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            PdTintedImage(
                drawable = if (checked) PdIcons.HeartFilled else PdIcons.Heart,
                tint = if (checked) brand else PdTheme.colors.content.secondary,
                size = PdIconSize.Regular.dp(),
                modifier = Modifier.graphicsLayer {
                    val pop = sin(PI * progress.value).toFloat()
                    val scale = 1f + (motion.popPeakScale - 1f) * pop
                    scaleX = scale
                    scaleY = scale
                },
            )
        }
    }
}
