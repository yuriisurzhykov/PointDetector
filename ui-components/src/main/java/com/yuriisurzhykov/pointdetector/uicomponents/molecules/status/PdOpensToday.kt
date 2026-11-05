package com.yuriisurzhykov.pointdetector.uicomponents.molecules.status

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** Closed now, but opens later today. */
@Immutable
class PdOpensToday(at: PdTextResolver) : PdBaseStatusLine(
    status = PlaceStatus.Closed,
    title = PdStringText(R.string.pd_status_opens_today, listOf(at)),
)
