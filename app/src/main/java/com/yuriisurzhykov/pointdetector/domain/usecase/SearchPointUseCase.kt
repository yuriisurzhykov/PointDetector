package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SearchPointUseCase {

    suspend fun searchPlaceByCondition(condition: String): Flow<List<Point>>

    open class Base @Inject constructor(
        private val pointsMapper: Mapper<List<PointCache>, List<Point>>,
        private val pointsRepository: PointsRepository
    ) : SearchPointUseCase {
        override suspend fun searchPlaceByCondition(condition: String): Flow<List<Point>> {
            return pointsRepository.fetchByCondition(condition).map { pointsMapper.map(it) }
        }
    }

}