package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FetchAllPointsUseCase {

    suspend fun fetchPoints(): Flow<List<PointUi>>

    open class Base @Inject constructor(
        private val pointsRepository: PointsRepository,
        private val mapper: SuspendMapper<List<PointCache>, List<PointUi>>
    ) : FetchAllPointsUseCase {
        override suspend fun fetchPoints(): Flow<List<PointUi>> {
            return pointsRepository.fetch().map { mapper.map(it) }
        }
    }

}