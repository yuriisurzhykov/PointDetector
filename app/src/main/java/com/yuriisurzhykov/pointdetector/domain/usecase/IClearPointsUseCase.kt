package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.data.repository.PointsRepository
import javax.inject.Inject

interface IClearPointsUseCase {

    suspend fun clearAll()

    open class Base @Inject constructor(private val pointRepository: PointsRepository) : IClearPointsUseCase {
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