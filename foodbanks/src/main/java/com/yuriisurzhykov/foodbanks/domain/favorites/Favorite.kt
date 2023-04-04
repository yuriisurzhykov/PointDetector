package com.yuriisurzhykov.foodbanks.domain.favorites

import com.yuriisurzhykov.foodbanks.core.presentation.Dispatchers
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesRepository
import kotlinx.coroutines.CoroutineScope

interface Favorite {

    interface Mapper<T> {

        fun map(id: Long, pointId: Long, name: String, address: String): T

        class FavoriteRemove(
            private val favoritesRepository: FavoritesRepository,
            private val dispatchers: Dispatchers,
            private val coroutineScope: CoroutineScope
        ) : Mapper<Unit> {

            override fun map(id: Long, pointId: Long, name: String, address: String) {
                dispatchers.launchBackground(coroutineScope) {
                    favoritesRepository.removeFavorite(id)
                }
            }
        }
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Base(
        private val id: Long,
        private val pointId: Long,
        private val name: String,
        private val address: String
    ) : Favorite {

        override fun <T> map(mapper: Mapper<T>): T {
            return mapper.map(id, pointId, name, address)
        }
    }
}