package com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Colours of the map style. The map is drawn by a third party, but its land, water, parks and
 * roads come from the theme, so the map is blue-tinted like the rest of the interface and
 * follows the day / night switch.
 */
@Immutable
data class MapColors(
    val land: Color,
    val water: Color,
    val park: Color,
    val road: Color,
    val edge: Color,
)
