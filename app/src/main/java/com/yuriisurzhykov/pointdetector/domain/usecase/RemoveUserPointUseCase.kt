package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.foodbanks.domain.points.UserPointsUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import javax.inject.Inject

interface RemoveUserPointUseCase {

    /**
     * Removes a place the person added, with its favorite mark. A place of the cloud is never
     * removed, whatever is asked.
     * @return true if the place is gone
     */
    suspend fun remove(point: PointUi): Boolean

    class Base @Inject constructor(
        private val userPointsUseCase: UserPointsUseCase
    ) : RemoveUserPointUseCase {

        override suspend fun remove(point: PointUi): Boolean =
            point.isUserPoint && userPointsUseCase.remove(point.id)
    }
}
