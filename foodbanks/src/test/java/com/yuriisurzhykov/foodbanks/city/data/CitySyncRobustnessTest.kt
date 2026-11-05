package com.yuriisurzhykov.foodbanks.city.data

import com.yuriisurzhykov.foodbanks.core.data.SnapshotValue
import com.yuriisurzhykov.foodbanks.core.data.map
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityKeysCloud
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityListReader
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityMetaCloud
import com.yuriisurzhykov.foodbanks.data.city.cloud.ValueCityMapper
import com.yuriisurzhykov.foodbanks.data.point.cloud.ValuePointMapper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The cloud data is edited by hand and grows: one entry that is not complete must not stop the
 * sync of everything else, and the list of cities must not need the points.
 */
class CitySyncRobustnessTest {

    private val mapper = ValueCityMapper.Base(ValuePointMapper.Base())

    private fun point(address: String, coordinates: Any? = mapOf("lat" to 45.6, "lng" to -122.6), hours: Any? = null) = mapOf(
        "address" to address,
        "placeName" to "Place $address",
        "coordinates" to coordinates,
        "workingHours" to (hours ?: listOf(hour("Mon", "09:00", "17:00"))),
    )

    private fun hour(day: String, from: String?, to: String?) =
        mapOf("dayName" to day, "dayValue" to -1L, "hoursFrom" to from, "hoursTo" to to)

    private fun city(code: String, points: Any? = null): Map<String, Any?> =
        mapOf("name" to code.replaceFirstChar { it.titlecase() }, "name_code" to "$code-wa-usa", "region" to "WA", "country" to "USA", "points" to points)

    // ---- points ----

    @Test
    fun `test a point without coordinates is left out and the others stay`() {
        val value = city("vancouver", listOf(point("1 Main St", coordinates = null), point("2 Main St")))

        val result = mapper.map(SnapshotValue.Base(value))

        assertEquals(listOf("2 Main St"), result.points.map { it.address })
    }

    @Test
    fun `test an hour without the start is left out and the point stays`() {
        val hours = listOf(hour("Mon", null, null), hour("Tue", "10:00", "14:00"))

        val result = mapper.map(SnapshotValue.Base(city("vancouver", listOf(point("1 Main St", hours = hours)))))

        val point = result.points.single()
        assertEquals(listOf("Tue"), point.workingHours.map { it.dayName })
    }

    @Test
    fun `test a city with only bad points still maps with no points`() {
        val result = mapper.map(SnapshotValue.Base(city("amboy", listOf(point("1 Main St", coordinates = null)))))

        assertTrue(result.points.isEmpty())
        assertEquals("amboy-wa-usa", result.nameCode)
    }

    @Test
    fun `test the fields of a city without points are enough to map it`() {
        val value = city("sacramento") - "points"

        val result = mapper.map(SnapshotValue.Base(value))

        assertEquals("Sacramento", result.name)
        assertTrue(result.points.isEmpty())
    }

    // ---- the list of cities ----

    @Test
    fun `test the list has every city that can be read`() = runBlocking {
        val reader = reader(keys = listOf("a", "b"), metas = mapOf("a" to city("a") - "points", "b" to city("b") - "points"))

        assertEquals(listOf("a-wa-usa", "b-wa-usa"), reader.cities().map { it.nameCode })
    }

    @Test
    fun `test a city that cannot be read is left out of the list`() = runBlocking {
        val reader = reader(
            keys = listOf("good", "broken", "empty"),
            metas = mapOf("good" to (city("good") - "points"), "broken" to mapOf("name" to "no code")),
        )

        assertEquals(listOf("good-wa-usa"), reader.cities().map { it.nameCode })
    }

    @Test
    fun `test a list that cannot be read at all is an error`() {
        val reader = CityListReader(
            keys = object : CityKeysCloud {
                override suspend fun keys(): List<String> = throw IllegalStateException("offline")
            },
            meta = FakeMeta(emptyMap()),
            valueMapper = mapper,
        )

        val failure = runCatching { runBlocking { reader.cities() } }.exceptionOrNull()

        assertTrue(failure is IllegalStateException)
    }

    private fun reader(keys: List<String>, metas: Map<String, Map<String, Any?>>) = CityListReader(
        keys = object : CityKeysCloud {
            override suspend fun keys(): List<String> = keys
        },
        meta = FakeMeta(metas),
        valueMapper = mapper,
    )

    private class FakeMeta(private val metas: Map<String, Map<String, Any?>>) : CityMetaCloud {
        override suspend fun meta(code: String): Map<String, Any?> = metas[code] ?: throw IllegalStateException("no $code")
    }
}
