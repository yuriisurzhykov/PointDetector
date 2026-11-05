package com.yuriisurzhykov.foodbanks.city.domain

import com.yuriisurzhykov.foodbanks.data.point.LatLng
import com.yuriisurzhykov.foodbanks.domain.location.DistanceCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class DistanceCalculatorTest {

    private val calculator = DistanceCalculator.Base()

    @Test
    fun `test distance between the same points is zero`() {
        val point = LatLng(38.58, -121.49)

        assertEquals(0.0, calculator.meters(point, point), 0.0)
    }

    @Test
    fun `test known distance between sacramento and san francisco`() {
        val sacramento = LatLng(38.5816, -121.4944)
        val sanFrancisco = LatLng(37.7749, -122.4194)

        // About 120 km by great circle
        assertEquals(120_000.0, calculator.meters(sacramento, sanFrancisco), 3_000.0)
    }

    @Test
    fun `test distance is symmetric`() {
        val first = LatLng(38.58, -121.49)
        val second = LatLng(45.52, -122.68)

        assertEquals(calculator.meters(first, second), calculator.meters(second, first), 0.001)
    }
}
