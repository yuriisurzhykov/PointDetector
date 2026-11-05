package com.yuriisurzhykov.pointdetector.uicomponents.model.map

import androidx.compose.runtime.Stable

/**
 * Where the person is. A list instead of a nullable point: [PdNoUser] has no spots, [PdUserAt]
 * has one.
 */
@Stable
interface PdMapUser {
    val spots: List<PdGeoPoint>
}
