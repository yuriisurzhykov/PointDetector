package com.yuriisurzhykov.foodbanks.di.domain

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yuriisurzhykov.foodbanks.data.city.CityRepository
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.foodbanks.data.location.CityGeocoder
import com.yuriisurzhykov.foodbanks.data.location.UserLocationSource
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import com.yuriisurzhykov.foodbanks.domain.city.CityCacheToDomainMapper
import com.yuriisurzhykov.foodbanks.domain.city.CityDetector
import com.yuriisurzhykov.foodbanks.domain.city.CityMatcher
import com.yuriisurzhykov.foodbanks.domain.city.SelectedCityUseCase
import com.yuriisurzhykov.foodbanks.domain.location.DistanceCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CityDetectionModule {

    @Provides
    @Singleton
    fun provideFusedLocationClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    /**
     * City names in the cloud are in English, so the geocoder must answer in English
     * regardless of the device language.
     */
    @Provides
    @Singleton
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder {
        return Geocoder(context, Locale.ENGLISH)
    }

    @Provides
    @Singleton
    fun provideUserLocationSource(client: FusedLocationProviderClient): UserLocationSource {
        return UserLocationSource.Base(client)
    }

    @Provides
    @Singleton
    fun provideCityGeocoder(geocoder: Geocoder): CityGeocoder {
        return CityGeocoder.Base(geocoder)
    }

    @Provides
    @Singleton
    fun provideDistanceCalculator(): DistanceCalculator {
        return DistanceCalculator.Base()
    }

    @Provides
    @Singleton
    fun provideCityMatcher(distanceCalculator: DistanceCalculator): CityMatcher {
        return CityMatcher.Base(distanceCalculator)
    }

    @Provides
    @Singleton
    fun provideCityDetector(
        locationSource: UserLocationSource,
        geocoder: CityGeocoder,
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
        cityCacheDataSource: CityCacheDataSource,
        detector: CityDetector,
        mapper: CityCacheToDomainMapper
    ): SelectedCityUseCase {
        return SelectedCityUseCase.Base(preference, cityCacheDataSource, detector, mapper)
    }
}
