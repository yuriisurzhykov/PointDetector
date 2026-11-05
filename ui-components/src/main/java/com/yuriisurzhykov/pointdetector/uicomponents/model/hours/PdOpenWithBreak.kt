package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** With extra data: two stretches with a break between them. The [summary] is prepared by a mapper. */
@Immutable
class PdOpenWithBreak(
    morning: PdSegment,
    afternoon: PdSegment,
    override val summary: PdTextResolver,
) : PdWorkingDay {
    override val segments: List<PdSegment> = listOf(morning, afternoon)
}
