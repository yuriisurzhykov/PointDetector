package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import javax.inject.Inject

interface ClearPointsUseCase {

    suspend fun clearAll()

    open class Base @Inject constructor(private val pointRepository: PointsRepository) : ClearPointsUseCase {
        override suspend fun clearAll() {
            val allItems = pointRepository.fetch()
            allItems.collect { list ->
                list.forEach { cachePoint ->
                    pointRepository.delete(cachePoint)
                }
            }
        }
    }

}