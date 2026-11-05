package com.yuriisurzhykov.pointdetector.uicomponents.model.map

import androidx.compose.runtime.Immutable

/** The person is at [point]. */
@Immutable
data class PdUserAt(val point: PdGeoPoint) : PdMapUser {
    override val spots: List<PdGeoPoint> = listOf(point)
}
