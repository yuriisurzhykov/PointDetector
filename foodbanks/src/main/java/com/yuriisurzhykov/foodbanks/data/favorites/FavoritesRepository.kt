package com.yuriisurzhykov.foodbanks.data.favorites

import com.yuriisurzhykov.foodbanks.data.point.cache.PointsCacheDataSource
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun fetch(): Flow<List<FavoritePointCache>>

    suspend fun markFavorite(pointId: Long): Boolean

    suspend fun removeFavorite(id: Long): Boolean

    abstract class Abstract(
        private val mapper: PointToFavoriteMapper,
        private val pointsCacheDataSource: PointsCacheDataSource,
        private val favoriteCacheDataStore: FavoriteCacheDataStore,
        private val favoritesDao: FavoritesDao
    ) : FavoritesRepository {

        override suspend fun fetch() = favoriteCacheDataStore.favorites()

        override suspend fun markFavorite(pointId: Long): Boolean {
            val favorite = mapper.map(pointsCacheDataSource.findById(pointId))
            return favoritesDao.insertFavorite(favorite)
        }

        override suspend fun removeFavorite(id: Long): Boolean {
            return favoritesDao.deleteFavorite(id)
        }
    }
}