package com.yuriisurzhykov.pointdetector.di

import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.configs.SortingTypeConfig
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.data.remote.LocalDistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import com.yuriisurzhykov.pointdetector.domain.services.PointsSortingTypeService
import com.yuriisurzhykov.pointdetector.domain.services.UserLocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
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

    /*@Provides
    @Singleton
    fun providePointsSortingTypeService(sortingTypeConfig: SortingTypeConfig): PointsSortingTypeService {
        return runBlocking {
            when (sortingTypeConfig.getConfigValue()) {
                PointsSortingTypeService.SortByDistance.sortingType -> PointsSortingTypeService.SortByDistance
            }
        }
    }*/
}