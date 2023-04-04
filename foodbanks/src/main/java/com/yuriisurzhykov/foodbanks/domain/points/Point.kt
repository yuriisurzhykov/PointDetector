package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.presentation.Dispatchers
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesRepository
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import kotlinx.coroutines.CoroutineScope

interface Point {

    interface Mapper<T> {
        fun map(id: Long, name: String, address: String, workingHours: List<WorkingHour>): T

        class MarkFavorite(
            private val favoritesRepository: FavoritesRepository,
            private val dispatchers: Dispatchers,
            private val coroutineScope: CoroutineScope
        ) : Mapper<Unit> {

            override fun map(
                id: Long, name: String, address: String, workingHours: List<WorkingHour>
            ) {
                dispatchers.launchBackground(coroutineScope) {
                    favoritesRepository.markFavorite(id)
                }
            }
        }
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Base(
        val id: Long,
        val name: String,
        val address: String,
        val workingHours: List<WorkingHour>
    ) : Point {

        override fun <T> map(mapper: Mapper<T>): T {
            return mapper.map(id, name, address, workingHours)
        }
    }
}