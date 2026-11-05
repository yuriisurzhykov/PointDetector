package com.yuriisurzhykov.pointdetector.uicomponents.model.map

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdMapMarker

/**
 * A place on the map: where it is and how it is drawn. The marker is a component, so every map
 * implementation draws the same pin. A [raised] place is drawn above the others (the selected one).
 */
@Immutable
class PdMapPlace(
    val id: Long,
    val position: PdGeoPoint,
    val marker: PdMapMarker,
    val raised: Boolean = false,
)
