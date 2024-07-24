package com.yuriisurzhykov.foodbanks.data.favorites

import com.yuriisurzhykov.foodbanks.data.point.cache.PointsCacheDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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
            return try {
                favoritesDao.insertFavorite(favorite)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        override suspend fun removeFavorite(id: Long): Boolean {
            // If number of deleted rows == 1, then the favorite deleted
            return favoritesDao.deleteFavorite(id) == 1
        }
    }

    class Base @Inject constructor(
        mapper: PointToFavoriteMapper,
        pointsCacheDataSource: PointsCacheDataSource,
        favoriteCacheDataStore: FavoriteCacheDataStore,
        favoritesDao: FavoritesDao
    ) : Abstract(mapper, pointsCacheDataSource, favoriteCacheDataStore, favoritesDao)
}