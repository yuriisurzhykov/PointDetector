package com.yuriisurzhykov.pointdetector.core

import kotlinx.coroutines.flow.Flow

interface Favorites<T> {
    suspend fun fetchFavorites(): Flow<List<T>>
}