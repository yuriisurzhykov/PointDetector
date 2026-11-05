package com.yuriisurzhykov.foodbanks.city.data

import com.yuriisurzhykov.foodbanks.core.data.MappingException
import com.yuriisurzhykov.foodbanks.core.data.SnapshotValue
import com.yuriisurzhykov.foodbanks.data.city.cloud.ValueCityMapper
import com.yuriisurzhykov.foodbanks.data.point.cloud.ValuePointMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ValueCityMapperTest {

    private lateinit var mapper: ValueCityMapper

    @Before
    fun `init resources for test`() {
        mapper = ValueCityMapper.Base(ValuePointMapper.Base())
    }

    @Test
    fun `test mapping city with points and working hours`() {
        val city = mapper.map(SnapshotValue.Base(cityValue(listOf(pointValue("Address 1")))))

        assertEquals("Sacramento", city.name)
        assertEquals("sacramento-ca-usa", city.nameCode)
        assertEquals("CA", city.region)
        assertEquals("United States", city.country)
        assertEquals(1, city.points.size)
        assertEquals("Address 1", city.points.first().address)
        assertEquals(38.67, city.points.first().coordinates.lat, 0.0)
        assertEquals(-121.29, city.points.first().coordinates.lng, 0.0)
        assertEquals("11:30", city.points.first().workingHours.first().hoursFrom)
    }

    @Test
    fun `test points get city code of their city`() {
        val city = mapper.map(SnapshotValue.Base(cityValue(listOf(pointValue("Address 1")))))

        assertEquals("sacramento-ca-usa", city.points.first().cityCode)
    }

    @Test
    fun `test points stored as keyed object are mapped like a list`() {
        val points = mapOf("-key1" to pointValue("Address 1"), "-key2" to pointValue("Address 2"))
        val city = mapper.map(SnapshotValue.Base(cityValue(points)))

        assertEquals(listOf("Address 1", "Address 2"), city.points.map { it.address })
    }

    @Test
    fun `test city without points has empty points list`() {
        val value = cityValue(null)
        assertEquals(emptyList<Any>(), mapper.map(SnapshotValue.Base(value)).points)
    }

    @Test
    fun `test city coordinates are optional`() {
        val without = mapper.map(SnapshotValue.Base(cityValue(null)))
        val with = mapper.map(
            SnapshotValue.Base(cityValue(null) + ("coordinates" to mapOf("lat" to 38.58, "lng" to -121.49)))
        )

        assertNull(without.coordinates)
        assertEquals(38.58, with.coordinates!!.lat, 0.0)
        assertEquals(-121.49, with.coordinates!!.lng, 0.0)
    }

    @Test(expected = MappingException::class)
    fun `test city without name code cannot be mapped`() {
        mapper.map(SnapshotValue.Base(mapOf("name" to "Sacramento")))
    }

    @Test
    fun `test firebase numbers of any type are accepted for coordinates`() {
        val point = pointValue("Address 1") + ("coordinates" to mapOf("lat" to 38L, "lng" to -121L))
        val city = mapper.map(SnapshotValue.Base(cityValue(listOf(point))))

        assertEquals(38.0, city.points.first().coordinates.lat, 0.0)
        assertEquals(-121.0, city.points.first().coordinates.lng, 0.0)
    }

    private fun cityValue(points: Any?): Map<String, Any?> = mapOf(
        "name" to "Sacramento",
        "name_code" to "sacramento-ca-usa",
        "region" to "CA",
        "country" to "United States",
        "points" to points
    )

    private fun pointValue(address: String): Map<String, Any?> = mapOf(
        "address" to address,
        "placeName" to "Place at $address",
        "coordinates" to mapOf("lat" to 38.67, "lng" to -121.29),
        "workingHours" to listOf(
            mapOf("dayName" to "Mon", "dayValue" to 1L, "hoursFrom" to "11:30", "hoursTo" to "15:00")
        )
    )
}
