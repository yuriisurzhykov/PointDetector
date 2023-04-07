package com.yuriisurzhykov.foodbanks.di.data

import com.yuriisurzhykov.foodbanks.data.favorites.FavoriteCacheDataStore
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesDao
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesRepository
import com.yuriisurzhykov.foodbanks.data.favorites.PointToFavoriteMapper
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsCacheDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FavoritesModule {

    @Provides
    @Singleton
    fun provideFavoriteCacheDataStore(dao: FavoritesDao): FavoriteCacheDataStore {
        return FavoriteCacheDataStore.Base(dao)
    }

    @Provides
    @Singleton
    fun provideFavoritesRepository(
        mapper: PointToFavoriteMapper,
        cache: PointsCacheDataSource,
        favoriteCache: FavoriteCacheDataStore,
        favoritesDao: FavoritesDao
    ): FavoritesRepository {
        return FavoritesRepository.Base(
            mapper, cache, favoriteCache, favoritesDao
        )
    }
}