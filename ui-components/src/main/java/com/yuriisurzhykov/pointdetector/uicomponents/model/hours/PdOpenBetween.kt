package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** One stretch of work from [from] to [until] (ready texts). */
@Immutable
class PdOpenBetween(segment: PdSegment, from: PdTextResolver, until: PdTextResolver) : PdWorkingDay {
    override val segments: List<PdSegment> = listOf(segment)
    override val summary: PdTextResolver = PdStringText(R.string.pd_hours_range, listOf(from, until))
}
