package com.yuriisurzhykov.pointdetector.uicomponents.molecules.status

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.model.hours.PdNextOpening
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/** With extra data: closed now, and the next opening is known. It is not a nullable parameter of a closed status. */
@Immutable
class PdClosedUntil(next: PdNextOpening) : PdBaseStatusLine(
    status = PlaceStatus.Closed,
    title = PdStringText(R.string.pd_status_closed),
) {
    private val hint = PdStringText(R.string.pd_status_next, listOf(next.day, next.hours))

    @Composable
    override fun ColumnScope.Hint() {
        HintText(hint)
    }
}
