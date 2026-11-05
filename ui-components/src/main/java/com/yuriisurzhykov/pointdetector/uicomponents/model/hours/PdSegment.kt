package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Immutable

/**
 * A stretch of the day axis as fractions from 0f to 1f. What the axis stands for is not known
 * to the UI. The part from [warnFrom] to [end] is drawn as "closing soon".
 */
@Immutable
data class PdSegment(val start: Float, val end: Float, val warnFrom: Float = end) {
    init {
        require(start in 0f..1f && end in start..1f && warnFrom in start..end) {
            "A segment needs 0 <= start <= warnFrom <= end <= 1, got $start, $warnFrom, $end"
        }
    }
}
