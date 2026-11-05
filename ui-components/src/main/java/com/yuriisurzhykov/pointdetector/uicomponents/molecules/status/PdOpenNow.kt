package com.yuriisurzhykov.pointdetector.uicomponents.molecules.status

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** Open, with the closing time ready as a text ("6 PM"). */
@Immutable
class PdOpenNow(until: PdTextResolver) : PdBaseStatusLine(
    status = PlaceStatus.Open,
    title = PdStringText(R.string.pd_status_open_until, listOf(until)),
)
