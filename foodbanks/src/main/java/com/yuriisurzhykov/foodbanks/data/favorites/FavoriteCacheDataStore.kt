package com.yuriisurzhykov.foodbanks.data.favorites

import kotlinx.coroutines.flow.Flow

interface FavoriteCacheDataStore {

    suspend fun favorites(): Flow<List<FavoritePointCache>>

    abstract class Abstract(
        private val dao: FavoritesDao
    ) : FavoriteCacheDataStore {

        override suspend fun favorites() = dao.favorites()

    }
}