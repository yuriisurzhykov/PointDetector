package com.yuriisurzhykov.pointdetector.di

import android.location.Address
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.mappers.*
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MappersModule {

    @Provides
    @Singleton
    fun provideCacheToUiPointsMapper(
        distanceCalculateService: DistanceCalculateService,
        userLocationService: IUserLocationService
    ): Mapper<PointCache, Point> {
        return CacheToUiPointMapper(distanceCalculateService, userLocationService)
    }

    @Provides
    @Singleton
    fun provideUiToCachePointsMapper(): Mapper<Point, PointCache> {
        return UiToCachePointMapper()
    }

    @Provides
    @Singleton
    fun provideCacheToUiListPointsMapper(cacheToUiPointMapper: CacheToUiPointMapper): Mapper<List<PointCache>, List<Point>> {
        return CacheToUiListPointsMapper(cacheToUiPointMapper)
    }

    @Provides
    @Singleton
    fun provideLocaleToGoogleLatLngMapper(): Mapper<LatLng, com.google.android.gms.maps.model.LatLng> {
        return LocalToGoogleLatLngMapper()
    }

    @Provides
    @Singleton
    fun provideAddressToPointMapper(
        distanceCalculateService: DistanceCalculateService,
        userLocationService: IUserLocationService
    ): Mapper<Address, Point> {
        return AddressToPointMapper(distanceCalculateService, userLocationService)
    }

}