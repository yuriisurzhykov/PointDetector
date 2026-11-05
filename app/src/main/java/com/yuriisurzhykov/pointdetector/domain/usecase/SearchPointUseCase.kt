package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SearchPointUseCase {

    suspend fun searchPlaceByCondition(condition: String): Flow<List<PointUi>>

    open class Base @Inject constructor(
        private val fetchAllPointsUseCase: FetchAllPointsUseCase
    ) : SearchPointUseCase {

        override suspend fun searchPlaceByCondition(condition: String): Flow<List<PointUi>> {
            return fetchAllPointsUseCase.fetchPoints().map { points ->
                points.filter {
                    it.placeName.contains(condition, ignoreCase = true)
                            || it.address.contains(condition, ignoreCase = true)
                }
            }
        }
    }

}
