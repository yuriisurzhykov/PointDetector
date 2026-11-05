package com.yuriisurzhykov.pointdetector.uicomponents.organisms.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdUserDot
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.pdOverlayAt
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdGeoPoint
import com.yuriisurzhykov.pointdetector.uicomponents.model.map.PdMapScene

/**
 * A flat sketch instead of a real map: the land colour and the places spread between their
 * extremes. It needs no network and no library, so previews, tests and a first run can use it.
 */
@Immutable
class PdSketchMap(private val scene: PdMapScene) : PdBaseComponent(), PdMap {

    @Composable
    override fun Render(modifier: Modifier) {
        val inset = PdTheme.dimensions.spacing.xxLarge
        BoxWithConstraints(
            modifier
                .fillMaxSize()
                .background(PdTheme.colors.map.land)
                .padding(bottom = scene.coveredBottom),
        ) {
            val density = LocalDensity.current
            val width = with(density) { (maxWidth - inset * 2).toPx() }
            val height = with(density) { (maxHeight - inset * 2).toPx() }
            val shift = with(density) { inset.toPx() }
            val points = scene.places.map { it.position } + scene.user.spots
            val minLat = points.minOfOrNull(PdGeoPoint::latitude) ?: 0.0
            val maxLat = points.maxOfOrNull(PdGeoPoint::latitude) ?: 0.0
            val minLng = points.minOfOrNull(PdGeoPoint::longitude) ?: 0.0
            val maxLng = points.maxOfOrNull(PdGeoPoint::longitude) ?: 0.0
            fun place(point: PdGeoPoint) = Offset(
                x = shift + width * fraction(point.longitude, minLng, maxLng),
                y = shift + height * (1f - fraction(point.latitude, minLat, maxLat)),
            )
            scene.places.sortedBy { it.raised }.forEach { place ->
                place.marker.Content(
                    Modifier
                        .pdOverlayAt { place(place.position) }
                        .clickable { scene.onPlaceClick(place.id) },
                )
            }
            scene.user.spots.forEach { spot -> PdUserDot.Content(Modifier.pdOverlayAt { place(spot) }) }
        }
    }

    private fun fraction(value: Double, min: Double, max: Double): Float =
        if (max - min < MIN_SPAN) HALF else ((value - min) / (max - min)).toFloat()

    private companion object {
        const val MIN_SPAN = 1e-9
        const val HALF = 0.5f
    }
}
