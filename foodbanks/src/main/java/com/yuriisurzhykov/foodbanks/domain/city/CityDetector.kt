package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.data.city.CityRepository
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.foodbanks.data.location.CityGeocoder
import com.yuriisurzhykov.foodbanks.data.location.LocationUnavailableException
import com.yuriisurzhykov.foodbanks.data.location.UserLocationSource
import com.yuriisurzhykov.foodbanks.data.point.LatLng
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

interface CityDetector {

    @kotlin.jvm.Throws(CityNotDetectedException::class)
    suspend fun detect(): CityCache

    abstract class Abstract(
        private val locationSource: UserLocationSource,
        private val geocoder: CityGeocoder,
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
         * Cities are cached after the first load, the repository is only needed
         * to fill an empty cache.
         */
        private suspend fun availableCities(): List<CityCache> {
            if (cityCacheDataSource.cities().isEmpty()) {
                cityRepository.fetch().collect()
            }
            return cityCacheDataSource.cities()
        }
    }

    class Base @Inject constructor(
        locationSource: UserLocationSource,
        geocoder: CityGeocoder,
        cityCacheDataSource: CityCacheDataSource,
        cityRepository: CityRepository,
        matcher: CityMatcher
    ) : Abstract(locationSource, geocoder, cityCacheDataSource, cityRepository, matcher)
}
