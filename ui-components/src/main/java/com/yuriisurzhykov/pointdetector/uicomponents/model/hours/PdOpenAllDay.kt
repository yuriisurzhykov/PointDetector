package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** Without data: open the whole day. */
@Immutable
object PdOpenAllDay : PdWorkingDay {
    override val segments: List<PdSegment> = listOf(PdSegment(start = 0f, end = 1f))
    override val summary: PdTextResolver = PdStringText(R.string.pd_open_all_day)
}
