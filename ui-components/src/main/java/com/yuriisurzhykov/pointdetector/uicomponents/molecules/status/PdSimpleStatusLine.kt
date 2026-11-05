package com.yuriisurzhykov.pointdetector.uicomponents.molecules.status

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A status with a title only. */
@Immutable
class PdSimpleStatusLine(
    status: PlaceStatus,
    title: PdTextResolver,
) : PdBaseStatusLine(status, title)
