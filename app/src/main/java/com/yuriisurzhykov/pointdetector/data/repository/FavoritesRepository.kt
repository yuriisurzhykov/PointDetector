package com.yuriisurzhykov.pointdetector.data.repository

import com.yuriisurzhykov.pointdetector.core.Favorites
import com.yuriisurzhykov.pointdetector.data.cache.PointsDao
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.presentation.favorites.FavoritesApply
import com.yuriisurzhykov.pointdetector.presentation.favorites.FavoritesRemove
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

interface FavoritesRepository :
    Favorites<PointCache>,
    FavoritesApply<PointCache>,
    FavoritesRemove<PointCache> {

    abstract class Abstract(private val pointsDao: PointsDao) : FavoritesRepository {
        override suspend fun fetchFavorites(): Flow<List<PointCache>> {
            return flow { emit(pointsDao.fetchFavorites()) }
        }

        override fun applyFavorite(item: PointCache) = runBlocking {
            val isFavorite = item.isFavorite.not()
            pointsDao.update(item.copy(isFavorite = isFavorite))
        }

        override fun remove(item: PointCache) = runBlocking {
            pointsDao.update(item.copy(isFavorite = false))
        }
    }

    class Base @Inject constructor(pointsDao: PointsDao) : Abstract(pointsDao)
}