package com.yuriisurzhykov.pointdetector.presentation.city

import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class CityPickerTest {

    private val sacramento = CityUi("sacramento", "Sacramento", "CA")
    private val oakland = CityUi("oakland", "Oakland", "CA")
    private val lviv = CityUi("lviv", "Lviv", "Lviv region")
    private val all = listOf(sacramento, oakland, lviv)

    // ---- the list state ----

    @Test
    fun `test an answer with cities shows them`() {
        assertEquals(CityListState.Loaded(all), CityListReducer.reduce(CityListState.Loading, all))
    }

    @Test
    fun `test the first answer without cities is a failure`() {
        assertSame(CityListState.Failed, CityListReducer.reduce(CityListState.Loading, null))
        assertSame(CityListState.Failed, CityListReducer.reduce(CityListState.Loading, emptyList()))
    }

    @Test
    fun `test a later failure does not take the shown cities away`() {
        val shown = CityListState.Loaded(all)

        assertSame(shown, CityListReducer.reduce(shown, null))
    }

    @Test
    fun `test a retry can recover from a failure`() {
        assertEquals(CityListState.Loaded(all), CityListReducer.reduce(CityListState.Failed, all))
    }

    // ---- the search ----

    @Test
    fun `test a blank query keeps every city`() {
        assertEquals(all, CityRows.matching(all, "  "))
    }

    @Test
    fun `test the query finds a city by name ignoring case`() {
        assertEquals(listOf(oakland), CityRows.matching(all, "oAk"))
    }

    @Test
    fun `test the query finds cities by region`() {
        assertEquals(listOf(sacramento, oakland), CityRows.matching(all, "ca"))
    }

    @Test
    fun `test a query with no match leaves nothing`() {
        assertTrue(CityRows.matching(all, "Tokyo").isEmpty())
    }
}
