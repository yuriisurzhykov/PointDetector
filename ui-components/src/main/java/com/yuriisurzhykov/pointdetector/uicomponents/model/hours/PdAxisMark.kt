package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A mark on the axis: a fraction and a ready label ("16:20", "6 AM"). */
@Immutable
data class PdAxisMark(val position: Float, val label: PdTextResolver) {
    init {
        require(position in 0f..1f) { "An axis mark needs a position in 0..1, got $position" }
    }
}
