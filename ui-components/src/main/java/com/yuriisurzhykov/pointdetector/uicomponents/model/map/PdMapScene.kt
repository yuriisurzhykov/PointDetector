package com.yuriisurzhykov.pointdetector.uicomponents.model.map

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Everything a map needs to know, with no map type in it. [coveredBottom] is the part of the map
 * hidden by a sheet, so the camera keeps its target in the visible part.
 */
@Immutable
class PdMapScene(
    val places: List<PdMapPlace>,
    val user: PdMapUser,
    val focus: PdMapFocus,
    val coveredBottom: Dp,
    val onPlaceClick: (Long) -> Unit,
)
