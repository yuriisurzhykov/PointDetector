package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.geo.LatLng
import com.yuriisurzhykov.geo.domain.DistanceCalculator
import javax.inject.Inject

interface CityMatcher {

    /**
     * Finds the city the user is in. A city with the name of the [locality] wins, and if
     * there are several of them (same name in different regions) the nearest one is taken.
     * Otherwise it is the nearest city, if it is close enough to be the user's city.
     */
    fun find(cities: List<CityCache>, location: LatLng, locality: String): CityCache?

    abstract class Abstract(
        private val distanceCalculator: DistanceCalculator,
        private val maxDistanceMeters: Double
    ) : CityMatcher {

        override fun find(cities: List<CityCache>, location: LatLng, locality: String): CityCache? {
            val named = cities.filter { it.name.equals(locality.trim(), ignoreCase = true) }
            if (named.isNotEmpty()) {
                return nearest(named, location)?.city ?: named.first()
            }
            return nearest(cities, location)
                ?.takeIf { it.meters <= maxDistanceMeters }
                ?.city
        }

        private fun nearest(cities: List<CityCache>, location: LatLng): Candidate? {
            return cities
                .mapNotNull { city ->
                    city.center()?.let { Candidate(city, distanceCalculator.meters(location, it)) }
                }
                .minByOrNull { it.meters }
        }

        private fun CityCache.center(): LatLng? {
            return LatLng(latitude ?: return null, longitude ?: return null)
        }

        private class Candidate(val city: CityCache, val meters: Double)
    }

    class Base @Inject constructor(
        distanceCalculator: DistanceCalculator
    ) : Abstract(distanceCalculator, MAX_DISTANCE_METERS) {

        private companion object {
            const val MAX_DISTANCE_METERS = 100_000.0
        }
    }
}
