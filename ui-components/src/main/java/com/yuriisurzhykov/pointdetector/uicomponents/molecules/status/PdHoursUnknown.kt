package com.yuriisurzhykov.pointdetector.uicomponents.molecules.status

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/** Without data: the place has no working hours at all, so nothing can be promised. */
@Immutable
object PdHoursUnknown : PdBaseStatusLine(
    status = PlaceStatus.Closed,
    title = PdStringText(R.string.pd_status_unknown),
)
