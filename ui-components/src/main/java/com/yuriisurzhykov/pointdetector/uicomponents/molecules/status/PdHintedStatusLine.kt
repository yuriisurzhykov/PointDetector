package com.yuriisurzhykov.pointdetector.uicomponents.molecules.status

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A status with extra data: a second line such as "Next: Thu 07:30-13:00". */
@Immutable
class PdHintedStatusLine(
    status: PlaceStatus,
    title: PdTextResolver,
    private val hint: PdTextResolver,
) : PdBaseStatusLine(status, title) {

    @Composable
    override fun ColumnScope.Hint() {
        HintText(hint)
    }
}
