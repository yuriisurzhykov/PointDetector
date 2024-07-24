package com.yuriisurzhykov.pointdetector.di

import android.content.Context
import android.location.Address
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursCache
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.mappers.AddressToPointMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.CacheToUiListPointsMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.CacheToUiPointMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.LocalToGoogleLatLngMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.PointUiToCacheMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.PointUiToDomainMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.UiToCachePointMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.WeekDayToCacheMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.WeekDayToWorkingHoursListMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.WorkingHourToWeekDayListMapper
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import com.yuriisurzhykov.pointdetector.domain.services.WeekDaysListResource
import com.yuriisurzhykov.pointdetector.domain.usecase.CheckPointAvailabilityUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.ConfigUnifiedSource
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        configUnifiedSource: ConfigUnifiedSource,
        availabilityUseCase: CheckPointAvailabilityUseCase.CachePoint,
        mapper: Mapper<List<WorkingHoursCache>, List<WeekDay>>,
        @ApplicationContext context: Context
    ): SuspendMapper<PointCache, PointUi> {
        return CacheToUiPointMapper(
            distanceCalculateService,
            userLocationService,
            configUnifiedSource,
            availabilityUseCase,
            mapper,
            context
        )
    }

    @Provides
    @Singleton
    fun provideUiToCachePointsMapper(mapper: Mapper<List<WeekDay>, List<WorkingHoursCache>>): Mapper<Point, PointCache> {
        return UiToCachePointMapper(mapper)
    }

    @Provides
    @Singleton
    fun provideCacheToUiListPointsMapper(cacheToUiPointMapper: CacheToUiPointMapper): SuspendMapper<List<PointCache>, List<PointUi>> {
        return CacheToUiListPointsMapper(cacheToUiPointMapper)
    }

    @Provides
    @Singleton
    fun provideLocaleToGoogleLatLngMapper(): Mapper<LatLng, com.google.android.gms.maps.model.LatLng> {
        return LocalToGoogleLatLngMapper()
    }

    @Provides
    @Singleton
    fun provideAddressToPointMapper(): Mapper<Address, Point> {
        return AddressToPointMapper()
    }

    @Provides
    @Singleton
    fun providePointsUIToPointMapper(): Mapper<PointUi, Point> {
        return PointUiToDomainMapper()
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

    @Provides
    @Singleton
    fun providePointUiToCacheMapper(mapper: WeekDayToCacheMapper): Mapper<PointUi, PointCache> =
        PointUiToCacheMapper(mapper)
}