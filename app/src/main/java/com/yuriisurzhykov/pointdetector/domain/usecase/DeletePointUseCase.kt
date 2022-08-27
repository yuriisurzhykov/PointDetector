package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

interface DeletePointUseCase {

    suspend fun delete(point: Point)

    open class Base @Inject constructor(
        private val pointsRepository: PointsRepository,
        private val mapper: Mapper<Point, PointCache>
    ) : DeletePointUseCase {
        override suspend fun delete(point: Point) {
            pointsRepository.deleteByAddressAndPlaceName(point.address, point.placeName)
        }
    }

}