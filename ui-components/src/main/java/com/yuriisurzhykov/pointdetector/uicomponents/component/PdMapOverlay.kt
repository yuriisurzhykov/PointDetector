package com.yuriisurzhykov.pointdetector.uicomponents.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.layout
import kotlin.math.roundToInt

/**
 * Centers a child on a pixel position inside its parent. The [position] is read while laying
 * out, so a map can move the overlay with every camera change without recomposing it.
 */
fun Modifier.pdOverlayAt(position: () -> Offset): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
    layout(placeable.width, placeable.height) {
        val center = position()
        placeable.place(
            (center.x - placeable.width / 2f).roundToInt(),
            (center.y - placeable.height / 2f).roundToInt(),
        )
    }
}
