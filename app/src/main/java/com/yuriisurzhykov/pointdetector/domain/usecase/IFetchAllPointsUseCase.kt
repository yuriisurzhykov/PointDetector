package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface IFetchAllPointsUseCase {

    suspend fun fetchPoints(): Flow<List<Point>>

    open class Base @Inject constructor(
        private val pointsRepository: PointsRepository,
        private val mapper: Mapper<List<PointCache>, List<Point>>
    ) : IFetchAllPointsUseCase {
        override suspend fun fetchPoints(): Flow<List<Point>> {
            return pointsRepository.fetch().map { mapper.map(it) }
        }
    }

}