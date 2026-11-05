package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.presentation.Dispatchers
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesRepository
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.data.point.cache.PointOrigin
import com.yuriisurzhykov.geo.LatLng
import kotlinx.coroutines.CoroutineScope

interface Point {

    interface Mapper<T> {

        fun map(
            id: Long,
            name: String,
            address: String,
            coordinates: LatLng,
            workingHours: List<WorkingHour>,
            origin: PointOrigin,
            favorite: Boolean
        ): T

        class MarkFavorite(
            private val favoritesRepository: FavoritesRepository,
            private val dispatchers: Dispatchers,
            private val coroutineScope: CoroutineScope
        ) : Mapper<Unit> {

            override fun map(
                id: Long,
                name: String,
                address: String,
                coordinates: LatLng,
                workingHours: List<WorkingHour>,
                origin: PointOrigin,
                favorite: Boolean
            ) {
                dispatchers.launchBackground(coroutineScope) {
                    favoritesRepository.markFavorite(id)
                }
            }
        }
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Base(
        private val id: Long,
        private val name: String,
        private val address: String,
        private val coordinates: LatLng,
        private val workingHours: List<WorkingHour>,
        private val origin: PointOrigin,
        private val favorite: Boolean
    ) : Point {

        override fun <T> map(mapper: Mapper<T>): T {
            return mapper.map(id, name, address, coordinates, workingHours, origin, favorite)
        }
    }
}
