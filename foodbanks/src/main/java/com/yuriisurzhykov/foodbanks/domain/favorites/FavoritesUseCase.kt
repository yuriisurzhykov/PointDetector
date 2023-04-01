package com.yuriisurzhykov.foodbanks.domain.favorites

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponse
import com.yuriisurzhykov.foodbanks.data.favorites.FavoriteCacheDataStore
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesRepository
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import com.yuriisurzhykov.foodbanks.domain.points.Point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface FavoritesUseCase {

    suspend fun favorites(): Flow<List<Favorite>>

    suspend fun markFavorite(point: Point)

    suspend fun detachFavorite(favorite: Favorite)

    abstract class Abstract(
        private val mapper: FavoriteCacheMapper,
        private val favoriteRepository: FavoritesRepository
    ) : FavoritesUseCase {

        override suspend fun favorites(): Flow<List<Favorite>> {
            return favoriteRepository.fetch().map { mapper.map(it) }
        }

        override suspend fun markFavorite(point: Point) {
            favoriteRepository.markFavorite(point.id)
        }

        override suspend fun detachFavorite(favorite: Favorite) {
            favoriteRepository.removeFavorite(favorite.id)
        }
    }
}