package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.domain.points.UserPointsUseCase
import com.yuriisurzhykov.geo.LatLng
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng as PointLatLng

class RemoveUserPointUseCaseTest {

    private val userPoints = FakeUserPoints()
    private val useCase = RemoveUserPointUseCase.Base(userPoints)

    private fun point(id: Long, user: Boolean) = PointUi(
        address = "Address",
        coordinates = PointLatLng(1.0, 2.0),
        distanceString = "1 km",
        distanceDouble = 1.0,
        placeName = "Place",
        favoriteState = FavoriteState.FavoriteDisabled,
        id = id,
        isUserPoint = user,
    )

    @Test
    fun `test a place of the person is removed`() = runBlocking {
        assertTrue(useCase.remove(point(5, user = true)))
        assertEquals(listOf(5L), userPoints.removed)
    }

    @Test
    fun `test a place of the cloud is never asked to be removed`() = runBlocking {
        assertFalse(useCase.remove(point(6, user = false)))
        assertTrue(userPoints.removed.isEmpty())
    }

    @Test
    fun `test the answer of the data is passed on when nothing was removed`() = runBlocking {
        userPoints.result = false
        assertFalse(useCase.remove(point(7, user = true)))
    }

    private class FakeUserPoints : UserPointsUseCase {
        val removed = mutableListOf<Long>()
        var result = true

        override suspend fun add(
            placeName: String,
            address: String,
            coordinates: LatLng,
            workingHours: List<WorkingHour>,
            help: Map<String, Boolean>,
        ) = Unit

        override suspend fun remove(pointId: Long): Boolean {
            removed += pointId
            return result
        }
    }
}
