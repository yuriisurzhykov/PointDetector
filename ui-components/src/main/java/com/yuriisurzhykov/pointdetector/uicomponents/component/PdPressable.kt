package com.yuriisurzhykov.pointdetector.uicomponents.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/**
 * Tap target with the project's press feedback: the element shrinks on a spring from
 * [PdTheme.motion] and comes back. Place it first in a modifier chain so everything after
 * it (background, border) scales together.
 */
@Composable
internal fun Modifier.pdPressable(role: Role, onClick: () -> Unit): Modifier {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val motion = PdTheme.motion
    val scale by animateFloatAsState(
        targetValue = if (pressed) motion.pressedScale else 1f,
        animationSpec = motion.spatial(),
        label = "press",
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(interactionSource = source, indication = null, role = role, onClick = onClick)
}
