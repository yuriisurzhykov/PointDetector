package com.yuriisurzhykov.geo.domain

import com.yuriisurzhykov.geo.LatLng
import javax.inject.Inject
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

interface DistanceCalculator {

    fun meters(from: LatLng, to: LatLng): Double

    /**
     * Haversine formula, the error of treating the Earth as a sphere is far below
     * what is needed to tell which city is the nearest one.
     */
    abstract class Abstract : DistanceCalculator {

        override fun meters(from: LatLng, to: LatLng): Double {
            val latitudeDelta = Math.toRadians(to.lat - from.lat)
            val longitudeDelta = Math.toRadians(to.lng - from.lng)
            val a = sin(latitudeDelta / 2).pow(2) +
                    cos(Math.toRadians(from.lat)) * cos(Math.toRadians(to.lat)) *
                    sin(longitudeDelta / 2).pow(2)
            return 2 * EARTH_RADIUS_METERS * asin(sqrt(a))
        }

        private companion object {
            const val EARTH_RADIUS_METERS = 6_371_000.0
        }
    }

    class Base @Inject constructor() : Abstract()
}
