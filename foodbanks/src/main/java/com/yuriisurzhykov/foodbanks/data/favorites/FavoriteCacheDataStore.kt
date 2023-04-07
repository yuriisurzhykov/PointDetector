package com.yuriisurzhykov.foodbanks.data.favorites

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FavoriteCacheDataStore {

    suspend fun favorites(): Flow<List<FavoritePointCache>>

    abstract class Abstract(
        private val dao: FavoritesDao
    ) : FavoriteCacheDataStore {

        override suspend fun favorites() = dao.favorites()

    }

    class Base @Inject constructor(dao: FavoritesDao) : Abstract(dao)
}