package com.yuriisurzhykov.pointdetector.uicomponents.model.map

/** What a map implementation lets the UI do with its camera. */
interface PdMapCamera {
    fun moveTo(point: PdGeoPoint)
}
