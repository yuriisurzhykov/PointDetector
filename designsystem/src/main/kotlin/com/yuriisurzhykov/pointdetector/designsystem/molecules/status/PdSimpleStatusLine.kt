package com.yuriisurzhykov.pointdetector.designsystem.molecules.status

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.designsystem.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdTextResolver

/** A status with a title only. */
@Immutable
class PdSimpleStatusLine(
    status: PlaceStatus,
    title: PdTextResolver,
) : PdBaseStatusLine(status, title)
