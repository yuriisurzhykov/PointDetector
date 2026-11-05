package com.yuriisurzhykov.pointdetector.presentation.points.details

import com.yuriisurzhykov.foodbanks.domain.points.UserPointsUseCase
import com.yuriisurzhykov.geo.LatLng
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.pointdetector.core.Dispatchers
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessage
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessages
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesApplyUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.RemoveUserPointUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng as PointLatLng

@OptIn(ExperimentalCoroutinesApi::class)
class PointDetailsViewModelTest {

    private val test = UnconfinedTestDispatcher()
    private val dispatchers = object : Dispatchers.Abstract(test, test) {}
    private val messages = AppMessages()
    private val told = mutableListOf<AppMessage>()
    private val userPoints = FakeUserPoints()
    private lateinit var viewModel: PointDetailsViewModel

    @Before
    fun setUp() {
        kotlinx.coroutines.Dispatchers.setMain(test)
        CoroutineScope(test).launch { messages.events.collect { told += it } }
        viewModel = PointDetailsViewModel(
            dispatchers,
            object : FavoritesApplyUseCase {
                override fun applyFavorite(item: PointUi) = Unit
            },
            messages,
            RemoveUserPointUseCase.Base(userPoints),
        )
    }

    @After
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    private fun point(id: Long, user: Boolean) = PointUi(
        address = "Address $id",
        coordinates = PointLatLng(1.0, 2.0),
        distanceString = "1 km",
        distanceDouble = 1.0,
        placeName = "Place $id",
        favoriteState = FavoriteState.FavoriteDisabled,
        id = id,
        isUserPoint = user,
    )

    @Test
    fun `test a place of the person is deleted, the person is told and the screen is closed`() {
        viewModel.show(point(7, user = true))
        var closed = false

        viewModel.delete { closed = true }

        assertEquals(listOf(7L), userPoints.removed)
        assertEquals(listOf<AppMessage>(AppMessage.PlaceDeleted), told)
        assertTrue(closed)
    }

    @Test
    fun `test a place of the cloud is never deleted`() {
        viewModel.show(point(8, user = false))
        var closed = false

        viewModel.delete { closed = true }

        assertTrue(userPoints.removed.isEmpty())
        assertEquals(listOf<AppMessage>(AppMessage.PlaceNotDeleted), told)
        assertFalse(closed)
    }

    @Test
    fun `test a place that could not be removed keeps the screen open and tells about it`() {
        userPoints.result = false
        viewModel.show(point(9, user = true))
        var closed = false

        viewModel.delete { closed = true }

        assertEquals(listOf<AppMessage>(AppMessage.PlaceNotDeleted), told)
        assertFalse(closed)
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
