package com.yuriisurzhykov.pointdetector.uicomponents.atoms.status

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.dp
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdMapMarker
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus

/**
 * The marker of a lone place, e.g. on the map of the place screen: the same circle as
 * [PdNumberedMapMarker], with a pin instead of the number, because such a place has no number.
 */
@Immutable
class PdPinMapMarker(private val status: PlaceStatus) : PdBaseComponent(), PdMapMarker {

    @Composable
    override fun Render(modifier: Modifier) {
        val component = PdTheme.dimensions.component
        val colors = PdTheme.colors
        val fill = if (status.isOpen) colors.primary.main else colors.surface.primary
        val edge = if (status.isOpen) colors.surface.primary else colors.status.neutral.main
        val tint = if (status.isOpen) colors.primary.onMain else colors.content.secondary
        val warning = colors.status.warning.main
        val halo = colors.primary.main.copy(alpha = PdTheme.opacity.halo)
        val ringStroke = with(LocalDensity.current) { component.mapPinStroke.toPx() }
        Box(modifier.size(component.mapPinHaloSize), contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(component.mapPinHaloSize)) { drawCircle(halo) }
            if (status is PlaceStatus.ClosingSoon) {
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
                PdTintedImage(PdIcons.Pin, tint, PdIconSize.Small.dp())
            }
        }
    }
}
