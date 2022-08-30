package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

interface SavePointUseCase {

    suspend fun save(entity: Point)

    open class Base @Inject constructor(
        private val pointsRepository: PointsRepository,
        private val mapper: Mapper<Point, PointCache>
    ) : SavePointUseCase {
        override suspend fun save(entity: Point) {
            pointsRepository.save(mapper.map(entity))
        }
    }

}