package com.yuriisurzhykov.geo.di.data

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yuriisurzhykov.geo.data.LocalityGeocoder
import com.yuriisurzhykov.geo.data.UserLocationSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {

    @Provides
    @Singleton
    fun provideFusedLocationClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    /**
     * Names in the cloud are in English, so the geocoder must answer in English
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
    fun provideLocalityGeocoder(geocoder: Geocoder): LocalityGeocoder {
        return LocalityGeocoder.Base(geocoder)
    }
}
