package com.yuriisurzhykov.pointdetector.uicomponents.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import kotlin.math.roundToInt

/**
 * Centers a child on a fraction of the available width without leaving the bounds. Used for
 * the labels of an axis, so a label near the edge stays inside instead of being cut off.
 */
internal fun Modifier.pdAxisPlacement(fraction: Float): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
    val free = (constraints.maxWidth - placeable.width).coerceAtLeast(0)
    layout(constraints.maxWidth, placeable.height) {
        val x = (fraction * constraints.maxWidth - placeable.width / 2f).roundToInt()
        placeable.place(x.coerceIn(0, free), 0)
    }
}
