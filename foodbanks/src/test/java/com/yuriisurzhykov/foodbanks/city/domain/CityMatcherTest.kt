package com.yuriisurzhykov.foodbanks.city.domain

import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.point.LatLng
import com.yuriisurzhykov.foodbanks.domain.city.CityMatcher
import com.yuriisurzhykov.foodbanks.domain.location.DistanceCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CityMatcherTest {

    private lateinit var matcher: CityMatcher

    private val sacramento = city("Sacramento", "sacramento-ca-usa", 38.58, -121.49)
    private val oakland = city("Oakland", "oakland-ca-usa", 37.80, -122.27)
    private val portland = city("Portland", "portland-or-usa", 45.52, -122.68)
    private val portlandMaine = city("Portland", "portland-me-usa", 43.66, -70.26)

    @Before
    fun `init resources for test`() {
        matcher = CityMatcher.Base(DistanceCalculator.Base())
    }

    @Test
    fun `test city with locality name is found`() {
        val found = matcher.find(listOf(oakland, sacramento), LatLng(38.5, -121.4), "Sacramento")

        assertEquals(sacramento, found)
    }

    @Test
    fun `test locality is compared ignoring case and spaces around`() {
        val found = matcher.find(listOf(oakland, sacramento), LatLng(38.5, -121.4), " sacramento ")

        assertEquals(sacramento, found)
    }

    @Test
    fun `test same name cities are resolved by distance`() {
        val cities = listOf(portlandMaine, portland)

        assertEquals(portland, matcher.find(cities, LatLng(45.5, -122.6), "Portland"))
        assertEquals(portlandMaine, matcher.find(cities, LatLng(43.7, -70.3), "Portland"))
    }

    @Test
    fun `test nearest city is used when locality is unknown`() {
        val found = matcher.find(listOf(oakland, sacramento), LatLng(38.6, -121.3), "")

        assertEquals(sacramento, found)
    }

    @Test
    fun `test nearest city is used when locality is not supported`() {
        val found = matcher.find(listOf(oakland, sacramento), LatLng(38.7, -121.3), "Citrus Heights")

        assertEquals(sacramento, found)
    }

    @Test
    fun `test too far nearest city is not the user's city`() {
        val found = matcher.find(listOf(oakland, sacramento), LatLng(35.68, 139.69), "")

        assertNull(found)
    }

    @Test
    fun `test city without center is ignored when searching nearest`() {
        val withoutCenter = city("Nowhere", "nowhere", null, null)
        val found = matcher.find(listOf(withoutCenter, sacramento), LatLng(38.6, -121.3), "")

        assertEquals(sacramento, found)
    }

    @Test
    fun `test city with locality name and without center is still found`() {
        val withoutCenter = city("Nowhere", "nowhere", null, null)
        val found = matcher.find(listOf(sacramento, withoutCenter), LatLng(38.6, -121.3), "Nowhere")

        assertEquals(withoutCenter, found)
    }

    @Test
    fun `test no cities`() {
        assertNull(matcher.find(emptyList(), LatLng(38.6, -121.3), "Sacramento"))
    }

    private fun city(name: String, code: String, latitude: Double?, longitude: Double?) =
        CityCache(0, name, code, "", "", latitude, longitude)
}
