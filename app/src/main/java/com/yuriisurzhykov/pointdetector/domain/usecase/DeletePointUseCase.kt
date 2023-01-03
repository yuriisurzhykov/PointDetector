package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

interface DeletePointUseCase {

    suspend fun delete(point: PointCache)

    open class Base @Inject constructor(
        private val pointsRepository: PointsRepository
    ) : DeletePointUseCase {
        override suspend fun delete(point: PointCache) {
            pointsRepository.deleteByAddressAndPlaceName(point.address, point.placeName)
        }
    }

}