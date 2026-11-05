package com.yuriisurzhykov.pointdetector.uicomponents.model.map

import androidx.compose.runtime.Immutable

/** Without data: leave the camera where it is. */
@Immutable
object PdNoFocus : PdMapFocus {
    override fun applyTo(camera: PdMapCamera) = Unit
}
