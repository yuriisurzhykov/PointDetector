package com.yuriisurzhykov.pointdetector.di

import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.LatLng
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.data.remote.LocalDistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import com.yuriisurzhykov.pointdetector.domain.services.UserLocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {

    @Provides
    @Singleton
    fun provideDistanceCalculateService(latLngMapper: Mapper<LatLng, com.google.android.gms.maps.model.LatLng>): DistanceCalculateService {
        return LocalDistanceCalculateService(latLngMapper)
    }

    @Provides
    @Singleton
    fun provideUserLocationService(): IUserLocationService {
        return UserLocationService()
    }

    @Provides
    @Singleton
    fun provideDispatchers(): Dispatchers {
        return Dispatchers.Base()
    }
}