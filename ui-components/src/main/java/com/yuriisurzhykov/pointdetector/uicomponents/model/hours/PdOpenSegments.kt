package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** With extra data: any number of stretches of work in one day. The [summary] is prepared by a mapper. */
@Immutable
class PdOpenSegments(
    override val segments: List<PdSegment>,
    override val summary: PdTextResolver,
) : PdWorkingDay {
    init {
        require(segments.isNotEmpty()) { "A day without work is PdDayOff" }
    }
}
