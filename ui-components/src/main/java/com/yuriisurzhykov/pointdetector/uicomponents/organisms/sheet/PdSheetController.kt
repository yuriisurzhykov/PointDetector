package com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animate
import androidx.compose.runtime.MutableFloatState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdSheetState

private const val FULL_TOLERANCE_PX = 0.5f

/**
 * Moves a sheet by drags and flings. A release projects where the sheet would stop and snaps to
 * the nearest of the three positions. [visible] is the single source of truth for the height.
 */
internal class PdSheetController(
    private val scope: CoroutineScope,
    private val visible: MutableFloatState,
    private val stops: PdSheetStops,
    private val spec: AnimationSpec<Float>,
    private val projectionSeconds: Float,
    private val onStateChange: (PdSheetState) -> Unit,
) {
    private var animation: Job? = null

    val isFull: Boolean get() = visible.floatValue >= stops.full - FULL_TOLERANCE_PX

    /** [delta] is how far the finger moved down. Returns the part the sheet used. */
    fun drag(delta: Float): Float {
        animation?.cancel()
        val next = (visible.floatValue - delta).coerceIn(stops.min, stops.full)
        val used = visible.floatValue - next
        visible.floatValue = next
        return used
    }

    /** [velocity] is in pixels per second, positive when the finger moves down. */
    fun settle(velocity: Float) {
        val target = stops.nearest(visible.floatValue - velocity * projectionSeconds)
        moveTo(target)
        onStateChange(target)
    }

    fun moveTo(state: PdSheetState) {
        animation?.cancel()
        animation = scope.launch {
            animate(visible.floatValue, stops.visible(state), animationSpec = spec) { value, _ ->
                visible.floatValue = value
            }
        }
    }
}
