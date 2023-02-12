package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.Favorites
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.FavoritesRepository
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FavoritesFetchUseCase : Favorites<PointUi> {

    abstract class Abstract constructor(
        private val mapper: SuspendMapper<PointCache, PointUi>,
        private val pointsRepository: FavoritesRepository
    ) : FavoritesFetchUseCase {
        override suspend fun fetchFavorites(): Flow<List<PointUi>> {
            return pointsRepository.fetchFavorites().map { list -> list.map { mapper.map(it) } }
        }
    }

    class Base @Inject constructor(
        mapper: SuspendMapper<PointCache, PointUi>,
        pointsRepository: FavoritesRepository
    ) : Abstract(mapper, pointsRepository)

}