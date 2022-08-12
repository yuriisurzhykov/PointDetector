package com.yuriisurzhykov.pointdetector.di

import android.content.Context
import android.location.Address
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.usecase.IFetchAllPointsUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.IGeoDecodeUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.ISavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.ISuggestedPlacesUseCase
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
    ): IFetchAllPointsUseCase {
        return IFetchAllPointsUseCase.Base(pointsRepository, mapper)
    }

    @Provides
    @Singleton
    fun provideSuggestedPlacesUseCase(
        @ApplicationContext context: Context,
        mapper: Mapper<Address, Point>
    ): ISuggestedPlacesUseCase {
        return ISuggestedPlacesUseCase.Base(context, 5, mapper)
    }

    @Provides
    @Singleton
    fun provideGeoDecodeUseCase(
        @ApplicationContext context: Context
    ): IGeoDecodeUseCase {
        return IGeoDecodeUseCase.Base(context, 5)
    }

    @Provides
    @Singleton
    fun provideSavePointUseCase(
        repository: PointsRepository,
        mapper: Mapper<Point, PointCache>
    ): ISavePointUseCase {
        return ISavePointUseCase.Base(repository, mapper)
    }

}