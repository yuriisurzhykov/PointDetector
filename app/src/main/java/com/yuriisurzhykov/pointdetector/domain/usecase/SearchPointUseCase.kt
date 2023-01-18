package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SearchPointUseCase {

    suspend fun searchPlaceByCondition(condition: String): Flow<List<PointUi>>

    open class Base @Inject constructor(
        private val pointsMapper: SuspendMapper<List<PointCache>, List<PointUi>>,
        private val pointsRepository: PointsRepository
    ) : SearchPointUseCase {
        override suspend fun searchPlaceByCondition(condition: String): Flow<List<PointUi>> {
            return pointsRepository.fetchByCondition(condition).map { pointsMapper.map(it) }
        }
    }

}