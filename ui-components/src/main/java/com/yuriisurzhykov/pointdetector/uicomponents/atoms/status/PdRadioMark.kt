package com.yuriisurzhykov.pointdetector.uicomponents.atoms.status

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.graphicsLayer
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent

/** A radio circle with a dot that springs in when it is chosen. */
@Immutable
class PdRadioMark(private val selected: Boolean) : PdBaseComponent(), PdSelectionMark {

    @Composable
    override fun Render(modifier: Modifier) {
        val component = PdTheme.dimensions.component
        val colors = PdTheme.colors
        val motion = PdTheme.motion
        val ring by animateColorAsState(
            targetValue = if (selected) colors.primary.main else colors.content.secondary,
            animationSpec = motion.quick(),
            label = "radio-ring",
        )
        val dot by animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = motion.spatial(),
            label = "radio-dot",
        )
        Box(
            modifier = modifier
                .size(component.radioSize)
                .border(PdTheme.dimensions.stroke.thick, ring, PdTheme.shapes.full),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier
                    .size(component.radioDot)
                    .graphicsLayer {
                        scaleX = dot
                        scaleY = dot
                    }
                    .clip(PdTheme.shapes.full)
                    .background(colors.primary.main),
            )
        }
    }
}
