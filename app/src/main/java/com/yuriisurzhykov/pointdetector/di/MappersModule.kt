package com.yuriisurzhykov.pointdetector.di

import android.location.Address
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursCache
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.mappers.*
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import com.yuriisurzhykov.pointdetector.domain.services.WeekDaysListResource
import com.yuriisurzhykov.pointdetector.domain.usecase.CheckPointAvailabilityUseCase
import com.yuriisurzhykov.pointsdetector.uicomponents.workday.entity.WeekDay
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
        userLocationService: IUserLocationService,
        availabilityUseCase: CheckPointAvailabilityUseCase,
        mapper: Mapper<List<WorkingHoursCache>, List<WeekDay>>
    ): Mapper<PointCache, Point> {
        return CacheToUiPointMapper(
            distanceCalculateService,
            userLocationService,
            availabilityUseCase,
            mapper
        )
    }

    @Provides
    @Singleton
    fun provideUiToCachePointsMapper(mapper: Mapper<List<WeekDay>, List<WorkingHoursCache>>): Mapper<Point, PointCache> {
        return UiToCachePointMapper(mapper)
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

    @Provides
    @Singleton
    fun provideWeekDayToWorkingHoursListMapper(): Mapper<List<WeekDay>, List<WorkingHoursCache>> {
        return WeekDayToWorkingHoursListMapper()
    }

    @Provides
    @Singleton
    fun provideWorkingHourToWeekDayListMapper(resource: WeekDaysListResource): Mapper<List<WorkingHoursCache>, List<WeekDay>> {
        return WorkingHourToWeekDayListMapper(resource)
    }

}