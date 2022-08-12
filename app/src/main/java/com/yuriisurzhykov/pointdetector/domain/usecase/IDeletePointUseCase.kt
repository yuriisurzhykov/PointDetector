package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

interface IDeletePointUseCase {

    suspend fun delete(point: Point)

    open class Base @Inject constructor(
        private val pointsRepository: PointsRepository,
        private val mapper: Mapper<Point, PointCache>
    ) : IDeletePointUseCase {
        override suspend fun delete(point: Point) {
            pointsRepository.delete(mapper.map(point))
        }
    }

}