package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.data.city.CityRepository
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.geo.data.LocalityGeocoder
import com.yuriisurzhykov.geo.data.LocationUnavailableException
import com.yuriisurzhykov.geo.data.UserLocationSource
import com.yuriisurzhykov.geo.LatLng
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

interface CityDetector {

    @kotlin.jvm.Throws(CityNotDetectedException::class)
    suspend fun detect(): CityCache

    abstract class Abstract(
        private val locationSource: UserLocationSource,
        private val geocoder: LocalityGeocoder,
        private val cityCacheDataSource: CityCacheDataSource,
        private val cityRepository: CityRepository,
        private val matcher: CityMatcher
    ) : CityDetector {

        override suspend fun detect(): CityCache {
            val location = userLocation()
            val locality = geocoder.locality(location)
            return matcher.find(availableCities(), location, locality)
                ?: throw CityNotDetectedException("there is no city near $location")
        }

        private suspend fun userLocation(): LatLng {
            try {
                return locationSource.location()
            } catch (e: LocationUnavailableException) {
                throw CityNotDetectedException("there is no user's location", e)
            }
        }

        /**
         * Detecting is an explicit act, so the list of cities is brought up to date first: a city
         * that appeared in the cloud after the cache was filled can be found too. Without a
         * network, or when the cloud fails, the cities that are cached are used.
         */
        private suspend fun availableCities(): List<CityCache> {
            try {
                cityRepository.fetch().collect()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // The cache is what there is.
            }
            return cityCacheDataSource.cities()
        }
    }

    class Base @Inject constructor(
        locationSource: UserLocationSource,
        geocoder: LocalityGeocoder,
        cityCacheDataSource: CityCacheDataSource,
        cityRepository: CityRepository,
        matcher: CityMatcher
    ) : Abstract(locationSource, geocoder, cityCacheDataSource, cityRepository, matcher)
}
