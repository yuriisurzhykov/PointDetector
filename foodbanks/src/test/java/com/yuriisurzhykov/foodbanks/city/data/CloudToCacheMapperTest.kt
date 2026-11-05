package com.yuriisurzhykov.foodbanks.city.data

import com.yuriisurzhykov.foodbanks.data.city.CloudToCacheMapper
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloud
import com.yuriisurzhykov.foodbanks.data.point.LatLng
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CloudToCacheMapperTest {

    private lateinit var mapper: CloudToCacheMapper

    @Before
    fun `init resources for test`() {
        mapper = CloudToCacheMapper.Base()
    }

    @Test
    fun `test city center is the average of its points`() {
        val city = mapper.map(
            city(point(LatLng(10.0, 20.0)), point(LatLng(20.0, 40.0)))
        )

        assertEquals(15.0, city.latitude!!, 0.0)
        assertEquals(30.0, city.longitude!!, 0.0)
    }

    @Test
    fun `test city without points has no center`() {
        val city = mapper.map(city())

        assertNull(city.latitude)
        assertNull(city.longitude)
    }

    @Test
    fun `test city fields are mapped`() {
        val city = mapper.map(city())

        assertEquals(0L, city.cityId)
        assertEquals("Sacramento", city.name)
        assertEquals("sacramento-ca-usa", city.nameCode)
        assertEquals("CA", city.region)
        assertEquals("United States", city.country)
    }

    private fun city(vararg points: PointCloud) = CityCloud(
        name = "Sacramento",
        region = "CA",
        nameCode = "sacramento-ca-usa",
        country = "United States",
        points = points.toList()
    )

    private fun point(coordinates: LatLng) = PointCloud(
        address = "Address",
        coordinates = coordinates,
        placeName = "Place",
        cityCode = "sacramento-ca-usa",
        workingHours = emptyList()
    )
}
