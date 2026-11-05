package com.yuriisurzhykov.pointdetector.uicomponents.model.map

import androidx.compose.runtime.Immutable

/** A point on the Earth as the UI knows it: no map library type leaks through the contract. */
@Immutable
data class PdGeoPoint(val latitude: Double, val longitude: Double) {
    init {
        require(latitude in MIN_LATITUDE..MAX_LATITUDE && longitude in MIN_LONGITUDE..MAX_LONGITUDE) {
            "Not a point on the Earth: $latitude, $longitude"
        }
    }

    private companion object {
        const val MIN_LATITUDE = -90.0
        const val MAX_LATITUDE = 90.0
        const val MIN_LONGITUDE = -180.0
        const val MAX_LONGITUDE = 180.0
    }
}
