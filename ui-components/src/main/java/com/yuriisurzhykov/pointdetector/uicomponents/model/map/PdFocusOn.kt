package com.yuriisurzhykov.pointdetector.uicomponents.model.map

import androidx.compose.runtime.Immutable

/** Move the camera to [point]; change [token] to ask for the same point once more. */
@Immutable
data class PdFocusOn(val point: PdGeoPoint, val token: Int = 0) : PdMapFocus {
    override fun applyTo(camera: PdMapCamera) = camera.moveTo(point)
}
