package com.yuriisurzhykov.pointdetector.di

import android.content.Context
import android.location.Address
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.IDateTimeSource
import com.yuriisurzhykov.pointdetector.domain.source.DateFormatterType
import com.yuriisurzhykov.pointdetector.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideFetchAllPointsUseCase(
        pointsRepository: PointsRepository,
        mapper: Mapper<List<PointCache>, List<Point>>
    ): FetchAllPointsUseCase {
        return FetchAllPointsUseCase.Base(pointsRepository, mapper)
    }

    @Provides
    @Singleton
    fun provideSuggestedPlacesUseCase(
        @ApplicationContext context: Context,
        mapper: Mapper<Address, Point>
    ): SuggestedPlacesUseCase {
        return SuggestedPlacesUseCase.Base(context, 5, mapper)
    }

    @Provides
    @Singleton
    fun provideGeoDecodeUseCase(
        @ApplicationContext context: Context
    ): GeoDecodeUseCase {
        return GeoDecodeUseCase.Base(context, 5)
    }

    @Provides
    @Singleton
    fun provideSavePointUseCase(
        repository: PointsRepository,
        mapper: Mapper<Point, PointCache>
    ): SavePointUseCase {
        return SavePointUseCase.Base(repository, mapper)
    }

    @Provides
    @Singleton
    fun provideSearchPointUseCase(
        repository: PointsRepository,
        mapper: Mapper<List<PointCache>, List<Point>>
    ): SearchPointUseCase {
        return SearchPointUseCase.Base(mapper, repository)
    }

    @Provides
    @Singleton
    fun provideCheckPointAvailabilityUseCase(
        formatter: IDateTimeSource.LocalDateTimeSource,
        useCase: CheckTimeAvailabilityUseCase
    ): CheckPointAvailabilityUseCase {
        return CheckPointAvailabilityUseCase.Base(formatter, useCase)
    }

    @Provides
    @Singleton
    fun provideCheckTimeAvailabilityUseCase(
        formatter: DateFormatterType.TimeFormat
    ): CheckTimeAvailabilityUseCase {
        return CheckTimeAvailabilityUseCase.Base(formatter)
    }

    @Provides
    @Singleton
    fun provideDeletePointUseCase(
        repository: PointsRepository,
        mapper: Mapper<Point, PointCache>
    ): DeletePointUseCase {
        return DeletePointUseCase.Base(repository, mapper)
    }

}