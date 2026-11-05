package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek

class PlacesModelTest {

    private val monday = PlacesClock(DayOfWeek.MONDAY, 12 * 60)

    private fun point(id: Long, hours: List<WeekDay>, favorite: Boolean = false) = PointUi(
        address = "Address $id",
        coordinates = LatLng(1.0, 2.0),
        distanceString = "1 km",
        distanceDouble = 1.0,
        placeName = "Place $id",
        workingHours = hours,
        favoriteState = FavoriteState.Factory.build(favorite),
        id = id,
    )

    private val openNow = point(1, listOf(WeekDay("Mon", 1, "09:00", "17:00")))
    private val closedNow = point(2, listOf(WeekDay("Tue", 2, "09:00", "17:00")), favorite = true)
    private val noHours = point(3, emptyList())

    private val all = listOf(openNow, closedNow, noHours)

    @Test
    fun `test counters of every tab do not depend on the selected tab`() {
        val model = PlacesModel.build(all, PlacesTab.Favorites, monday)

        assertEquals(3, model.counts.getValue(PlacesTab.All))
        assertEquals(1, model.counts.getValue(PlacesTab.OpenNow))
        assertEquals(1, model.counts.getValue(PlacesTab.Favorites))
    }

    @Test
    fun `test the open tab keeps only the places that can be visited now`() {
        val rows = PlacesModel.build(all, PlacesTab.OpenNow, monday).rows

        assertEquals(listOf(1L), rows.map { it.point.id })
        assertTrue(rows.single().standing.isOpen)
    }

    @Test
    fun `test the favorites tab keeps only favorites`() {
        assertEquals(listOf(2L), PlacesModel.build(all, PlacesTab.Favorites, monday).rows.map { it.point.id })
    }

    @Test
    fun `test the all tab keeps the order of the list`() {
        assertEquals(listOf(1L, 2L, 3L), PlacesModel.build(all, PlacesTab.All, monday).rows.map { it.point.id })
    }

    @Test
    fun `test a toggled favorite flips the state both ways`() {
        assertEquals(FavoriteState.FavoriteEnabled, openNow.toggledFavorite().favoriteState)
        assertEquals(FavoriteState.FavoriteDisabled, closedNow.toggledFavorite().favoriteState)
    }
}
