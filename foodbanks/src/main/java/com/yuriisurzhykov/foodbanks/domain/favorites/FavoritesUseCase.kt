package com.yuriisurzhykov.foodbanks.domain.favorites

import com.yuriisurzhykov.foodbanks.core.presentation.Dispatchers
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FavoritesUseCase {

    suspend fun favorites(): Flow<List<Favorite>>

    suspend fun detachFavorite(favorite: Favorite, coroutineScope: CoroutineScope)

    /**
     * Whether the point is a favorite is a part of the point itself, so the screens that
     * already have a point can change it without looking for the favorite first.
     * @return true if the favorite is changed
     */
    suspend fun markFavorite(pointId: Long): Boolean

    suspend fun unmarkFavorite(pointId: Long): Boolean

    abstract class Abstract(
        private val mapper: FavoriteCacheMapper,
        private val dispatchers: Dispatchers,
        private val favoriteRepository: FavoritesRepository
    ) : FavoritesUseCase {

        override suspend fun favorites(): Flow<List<Favorite>> {
            return favoriteRepository.fetch().map { mapper.map(it) }
        }

        override suspend fun detachFavorite(favorite: Favorite, coroutineScope: CoroutineScope) {
            favorite.map(
                Favorite.Mapper.FavoriteRemove(favoriteRepository, dispatchers, coroutineScope)
            )
        }

        override suspend fun markFavorite(pointId: Long) = favoriteRepository.markFavorite(pointId)

        override suspend fun unmarkFavorite(pointId: Long) =
            favoriteRepository.unmarkFavorite(pointId)
    }

    class Base @Inject constructor(
        mapper: FavoriteCacheMapper,
        dispatchers: Dispatchers,
        favoriteRepository: FavoritesRepository
    ) : Abstract(mapper, dispatchers, favoriteRepository)
}
