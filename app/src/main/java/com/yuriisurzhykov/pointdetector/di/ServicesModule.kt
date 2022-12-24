package com.yuriisurzhykov.pointdetector.di

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.data.remote.LocalDistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import com.yuriisurzhykov.pointdetector.domain.services.UserLocationService
import com.yuriisurzhykov.pointdetector.domain.usecase.PointsFetchValueListener
import com.yuriisurzhykov.pointdetector.domain.usecase.PointsFirebaseListFetchListener
import com.yuriisurzhykov.pointdetector.domain.usecase.SavePointUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context

    /*@Provides
    @Singleton
    fun providePointsSortingTypeService(sortingTypeConfig: SortingTypeConfig): PointsSortingTypeService {
        return runBlocking {
            when (sortingTypeConfig.getConfigValue()) {
                PointsSortingTypeService.SortByDistance.sortingType -> PointsSortingTypeService.SortByDistance
            }
        }
    }*/

    @Singleton
    @Provides
    fun providePointsFetchValueListener(
        listMapper: Mapper<DataSnapshot, List<Point>>,
        useCase: SavePointUseCase,
        dispatchers: Dispatchers
    ): PointsFetchValueListener {
        return PointsFirebaseListFetchListener(listMapper, dispatchers, useCase)
    }
}