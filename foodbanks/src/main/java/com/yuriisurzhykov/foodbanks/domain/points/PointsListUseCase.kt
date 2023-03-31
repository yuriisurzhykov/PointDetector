package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponse

interface PointsListUseCase {

    suspend fun points(): Flow<UseCaseResponse<List<Point>>>

    abstract class Abstract(
        private val mapper:
    ): PointsListUseCase
}