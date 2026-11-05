package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.Favorites
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FavoritesFetchUseCase : Favorites<PointUi> {

    /**
     * Favorites of the selected city, like all the points.
     */
    abstract class Abstract constructor(
        private val fetchAllPointsUseCase: FetchAllPointsUseCase
    ) : FavoritesFetchUseCase {

        override suspend fun fetchFavorites(): Flow<List<PointUi>> {
            return fetchAllPointsUseCase.fetchPoints().map { points ->
                points.filter { it.favoriteState == FavoriteState.FavoriteEnabled }
            }
        }
    }

    class Base @Inject constructor(
        fetchAllPointsUseCase: FetchAllPointsUseCase
    ) : Abstract(fetchAllPointsUseCase)

}
