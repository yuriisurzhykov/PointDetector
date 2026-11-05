package com.yuriisurzhykov.pointdetector.uicomponents.model.map

import androidx.compose.runtime.Stable

/**
 * An order for the camera. It is a value: the map applies a new one when it differs from the
 * previous, which is why [PdFocusOn] carries a token to ask for the same point again.
 */
@Stable
interface PdMapFocus {
    fun applyTo(camera: PdMapCamera)
}
