package com.yuriisurzhykov.pointdetector.di

import com.yuriisurzhykov.pointdetector.data.cache.PointsDao
import com.yuriisurzhykov.pointdetector.data.repository.FavoritesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideFavoritesRepository(pointsDao: PointsDao): FavoritesRepository {
        return FavoritesRepository.Base(pointsDao)
    }

}