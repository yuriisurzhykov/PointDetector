package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** Without data: the place does not work that day. */
@Immutable
object PdDayOff : PdWorkingDay {
    override val segments: List<PdSegment> = emptyList()
    override val summary: PdTextResolver = PdStringText(R.string.pd_day_off)
}
