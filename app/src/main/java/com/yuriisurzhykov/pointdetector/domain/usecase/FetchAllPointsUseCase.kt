package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponse
import com.yuriisurzhykov.foodbanks.domain.city.CityNotDetectedException
import com.yuriisurzhykov.foodbanks.domain.city.SelectedCityUseCase
import com.yuriisurzhykov.foodbanks.domain.points.Point
import com.yuriisurzhykov.foodbanks.domain.points.PointsListUseCase
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessage
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessages
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

interface FetchAllPointsUseCase {

    /**
     * Points of the selected city, the cloud ones and the ones added by the user together.
     * The city is detected if the user hasn't chosen one. When there is no city, there are no
     * points, and the user can pick the city by hand.
     */
    suspend fun fetchPoints(): Flow<List<PointUi>>

    open class Base @Inject constructor(
        private val selectedCityUseCase: SelectedCityUseCase,
        private val pointsListUseCase: PointsListUseCase,
        private val mapper: SuspendMapper<List<Point>, List<PointUi>>,
        private val messages: AppMessages
    ) : FetchAllPointsUseCase {

        override suspend fun fetchPoints(): Flow<List<PointUi>> = flow {
            if (hasCity()) {
                emitAll(
                    pointsListUseCase.points()
                        .onEach { tell(it) }
                        .mapNotNull { it.points() }
                        .map { mapper.map(it) }
                )
            } else {
                emit(emptyList())
            }
        }

        private suspend fun hasCity(): Boolean {
            return try {
                selectedCityUseCase.selectedCity()
                true
            } catch (e: CityNotDetectedException) {
                false
            }
        }

        /** The cached points are shown either way; the person is told why the cloud did not answer. */
        private fun tell(response: UseCaseResponse<List<Point>>) {
            when (response) {
                is UseCaseResponse.NoNetwork -> messages.post(AppMessage.NoConnection)
                is UseCaseResponse.NetworkError -> messages.post(AppMessage.SyncFailed)
                else -> Unit
            }
        }

        /**
         * The error response has nothing to show, the cached points are already emitted before it.
         */
        private fun UseCaseResponse<List<Point>>.points(): List<Point>? = when (this) {
            is UseCaseResponse.Success -> data
            is UseCaseResponse.NoNetwork -> data
            is UseCaseResponse.NetworkError -> null
            else -> null
        }
    }

}
