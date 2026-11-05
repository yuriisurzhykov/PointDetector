package com.yuriisurzhykov.foodbanks.di.domain

import com.yuriisurzhykov.foodbanks.data.city.CityRepository
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.foodbanks.data.prefs.CityModePreference
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import com.yuriisurzhykov.foodbanks.domain.city.CityCacheToDomainMapper
import com.yuriisurzhykov.foodbanks.domain.city.CityDetector
import com.yuriisurzhykov.foodbanks.domain.city.CityMatcher
import com.yuriisurzhykov.foodbanks.domain.city.SelectedCityUseCase
import com.yuriisurzhykov.geo.data.LocalityGeocoder
import com.yuriisurzhykov.geo.data.UserLocationSource
import com.yuriisurzhykov.geo.domain.DistanceCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CityDetectionModule {

    @Provides
    @Singleton
    fun provideCityMatcher(distanceCalculator: DistanceCalculator): CityMatcher {
        return CityMatcher.Base(distanceCalculator)
    }

    @Provides
    @Singleton
    fun provideCityDetector(
        locationSource: UserLocationSource,
        geocoder: LocalityGeocoder,
        cityCacheDataSource: CityCacheDataSource,
        cityRepository: CityRepository,
        matcher: CityMatcher
    ): CityDetector {
        return CityDetector.Base(
            locationSource, geocoder, cityCacheDataSource, cityRepository, matcher
        )
    }

    @Provides
    @Singleton
    fun provideCityCacheToDomainMapper(): CityCacheToDomainMapper {
        return CityCacheToDomainMapper.Base()
    }

    @Provides
    @Singleton
    fun provideSelectedCityUseCase(
        preference: SelectedCityPreference,
        modePreference: CityModePreference,
        cityCacheDataSource: CityCacheDataSource,
        detector: CityDetector,
        mapper: CityCacheToDomainMapper
    ): SelectedCityUseCase {
        return SelectedCityUseCase.Base(preference, modePreference, cityCacheDataSource, detector, mapper)
    }
}
