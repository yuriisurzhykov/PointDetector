package com.yuriisurzhykov.pointdetector.uicomponents.atoms.status

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText

/**
 * A numbered circle for the map; the number matches the card in the list. A place that can be
 * visited is filled with the brand colour, a closed one is an outline, and "closing soon" gets an
 * extra ring, so the state never rests on colour alone. The selected marker grows and has a halo.
 */
@Immutable
class PdNumberedMapMarker(
    private val number: Int,
    private val status: PlaceStatus,
    private val selected: Boolean = false,
) : PdBaseComponent(), PdMapMarker {

    @Composable
    override fun Render(modifier: Modifier) {
        val component = PdTheme.dimensions.component
        val colors = PdTheme.colors
        val label = remember(number) { PdLiteralText(number.toString()) }
        val scale by animateFloatAsState(
            targetValue = if (selected) component.mapPinSelectedScale else 1f,
            animationSpec = PdTheme.motion.spatial(),
            label = "marker",
        )
        val fill = if (status.isOpen) colors.primary.main else colors.surface.primary
        val edge = if (status.isOpen) colors.surface.primary else colors.status.neutral.main
        val text = if (status.isOpen) colors.primary.onMain else colors.content.secondary
        val warning = colors.status.warning.main
        val halo = colors.primary.main.copy(alpha = PdTheme.opacity.halo)
        val isClosingSoon = status is PlaceStatus.ClosingSoon
        Box(
            modifier = modifier
                .size(component.mapPinHaloSize)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Canvas(Modifier.size(component.mapPinHaloSize)) { drawCircle(halo) }
            }
            if (isClosingSoon) {
                val ringStroke = with(LocalDensity.current) { component.mapPinStroke.toPx() }
                Canvas(Modifier.size(component.mapPinRingSize)) {
                    drawCircle(
                        color = warning,
                        radius = (size.minDimension - ringStroke) / 2f,
                        center = Offset(size.width / 2f, size.height / 2f),
                        style = Stroke(ringStroke),
                    )
                }
            }
            Box(
                Modifier
                    .size(component.mapPinSize)
                    .clip(PdTheme.shapes.full)
                    .background(fill)
                    .border(component.mapPinStroke, edge, PdTheme.shapes.full),
                contentAlignment = Alignment.Center,
            ) {
                PdBasicText(label, PdTextRole.LabelMedium.style(), text, maxLines = 1)
            }
        }
    }
}
