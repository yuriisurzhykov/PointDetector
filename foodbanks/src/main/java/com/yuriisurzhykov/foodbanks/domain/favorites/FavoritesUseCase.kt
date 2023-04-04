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
    }

    class Base @Inject constructor(
        mapper: FavoriteCacheMapper,
        dispatchers: Dispatchers,
        favoriteRepository: FavoritesRepository
    ) : Abstract(mapper, dispatchers, favoriteRepository)
}