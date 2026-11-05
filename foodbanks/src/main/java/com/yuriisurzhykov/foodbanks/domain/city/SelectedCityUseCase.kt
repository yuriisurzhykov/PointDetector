package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.foodbanks.data.prefs.CityModePreference
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

    /**
     * The user's own choice, it is used from now on instead of the detected city. The city is no
     * more an automatic one.
     */
    suspend fun select(cityCode: String)

    /** True while the city is the one the app took by the location, false after the person picked one. */
    suspend fun isAutomatic(): Boolean

    /**
     * Forgets the choice and detects the city where the user is now, even when a city was chosen
     * before. A failed detection keeps the previous choice.
     */
    @kotlin.jvm.Throws(CityNotDetectedException::class)
    suspend fun detect(): City

    abstract class Abstract(
        private val preference: SelectedCityPreference,
        private val modePreference: CityModePreference,
        private val cityCacheDataSource: CityCacheDataSource,
        private val detector: CityDetector,
        private val mapper: CityCacheToDomainMapper
    ) : SelectedCityUseCase {

        override suspend fun selectedCity(): City {
            val city = savedCity() ?: detectedCity()
            return mapper.mapSingle(city)
        }

        override suspend fun select(cityCode: String) {
            preference.savePref(cityCode)
            modePreference.savePref(CityModePreference.MANUAL)
        }

        override suspend fun isAutomatic(): Boolean = try {
            modePreference.getPrefValue() == CityModePreference.AUTOMATIC
        } catch (e: NoPreferenceException) {
            false
        }

        override suspend fun detect(): City = mapper.mapSingle(detectedCity())

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
            modePreference.savePref(CityModePreference.AUTOMATIC)
            return city
        }
    }

    class Base @Inject constructor(
        preference: SelectedCityPreference,
        modePreference: CityModePreference,
        cityCacheDataSource: CityCacheDataSource,
        detector: CityDetector,
        mapper: CityCacheToDomainMapper
    ) : Abstract(preference, modePreference, cityCacheDataSource, detector, mapper)
}
