package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.foodbanks.data.prefs.NoPreferenceException
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import javax.inject.Inject

interface SelectedCityUseCase {

    /**
     * The city chosen by the user. When there is no choice yet, the city where the user is
     * now is detected and remembered, so the user doesn't have to pick it manually.
     */
    @kotlin.jvm.Throws(CityNotDetectedException::class)
    suspend fun selectedCity(): City

    abstract class Abstract(
        private val preference: SelectedCityPreference,
        private val cityCacheDataSource: CityCacheDataSource,
        private val detector: CityDetector,
        private val mapper: CityCacheToDomainMapper
    ) : SelectedCityUseCase {

        override suspend fun selectedCity(): City {
            val city = savedCity() ?: detectedCity()
            return mapper.mapSingle(city)
        }

        private suspend fun savedCity(): CityCache? {
            return try {
                cityCacheDataSource.city(preference.getPrefValue())
            } catch (e: NoPreferenceException) {
                null
            }
        }

        private suspend fun detectedCity(): CityCache {
            val city = detector.detect()
            preference.savePref(city.nameCode)
            return city
        }
    }

    class Base @Inject constructor(
        preference: SelectedCityPreference,
        cityCacheDataSource: CityCacheDataSource,
        detector: CityDetector,
        mapper: CityCacheToDomainMapper
    ) : Abstract(preference, cityCacheDataSource, detector, mapper)
}
