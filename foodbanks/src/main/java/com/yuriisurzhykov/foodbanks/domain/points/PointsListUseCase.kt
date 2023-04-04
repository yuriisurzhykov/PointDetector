package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponse
import com.yuriisurzhykov.foodbanks.core.presentation.Dispatchers
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesRepository
import com.yuriisurzhykov.foodbanks.data.point.PointsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PointsListUseCase {

    suspend fun points(): Flow<UseCaseResponse<List<Point>>>

    suspend fun markFavorite(point: Point, coroutineScope: CoroutineScope)

    abstract class Abstract(
        private val pointsRepository: PointsRepository,
        private val favoritesRepository: FavoritesRepository,
        private val mapper: PointsUseCaseResponseMapper,
        private val dispatchers: Dispatchers
    ) : PointsListUseCase {

        override suspend fun points() = pointsRepository.fetch().map { mapper.map(it) }

        override suspend fun markFavorite(point: Point, coroutineScope: CoroutineScope) {
            point.map(Point.Mapper.MarkFavorite(favoritesRepository, dispatchers, coroutineScope))
        }
    }
}