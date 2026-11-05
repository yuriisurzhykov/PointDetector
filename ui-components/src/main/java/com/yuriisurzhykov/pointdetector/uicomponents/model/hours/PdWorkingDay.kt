package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Stable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** What a working day consists of. A day off is [PdDayOff], not a null. */
@Stable
interface PdWorkingDay {
    val segments: List<PdSegment>

    /** "09:00 - 17:00" or "Day off", ready for display. */
    val summary: PdTextResolver
}
