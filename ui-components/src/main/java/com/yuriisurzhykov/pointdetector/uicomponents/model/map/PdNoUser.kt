package com.yuriisurzhykov.pointdetector.uicomponents.model.map

import androidx.compose.runtime.Immutable

/** Without data: the location is not known (yet), so no dot is drawn. */
@Immutable
object PdNoUser : PdMapUser {
    override val spots: List<PdGeoPoint> = emptyList()
}
