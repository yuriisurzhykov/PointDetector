package com.yuriisurzhykov.foodbanks.domain.favorites

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponse
import com.yuriisurzhykov.foodbanks.domain.points.Point
import kotlinx.coroutines.flow.Flow

interface FavoritesUseCase {

    suspend fun favorites(): Flow<UseCaseResponse<Favorite>>

    suspend fun markFavorite(point: Point)

    suspend fun detachFavorite(favorite: Favorite)
}