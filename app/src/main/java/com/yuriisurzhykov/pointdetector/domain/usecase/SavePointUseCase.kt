package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import javax.inject.Inject

interface SavePointUseCase {

    suspend fun save(entity: PointCache)

    open class Base @Inject constructor(
        private val pointsRepository: PointsRepository
    ) : SavePointUseCase {
        override suspend fun save(entity: PointCache) {
            pointsRepository.save(entity)
        }
    }

}