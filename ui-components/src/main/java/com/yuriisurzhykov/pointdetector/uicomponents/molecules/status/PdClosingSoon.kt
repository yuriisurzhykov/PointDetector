package com.yuriisurzhykov.pointdetector.uicomponents.molecules.status

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/** Open, but the last minutes: [minutesLeft] is a number, the rule "what is soon" lives in a mapper. */
@Immutable
class PdClosingSoon(minutesLeft: Int) : PdBaseStatusLine(
    status = PlaceStatus.ClosingSoon,
    title = PdStringText(R.string.pd_status_closing_soon, listOf(PdLiteralText(minutesLeft.toString()))),
) {
    init {
        require(minutesLeft >= 0) { "Minutes left cannot be negative: $minutesLeft" }
    }
}
