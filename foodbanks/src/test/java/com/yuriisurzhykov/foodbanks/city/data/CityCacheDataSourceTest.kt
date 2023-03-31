package com.yuriisurzhykov.foodbanks.city.data

import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.foodbanks.data.city.cache.CityDao
import com.yuriisurzhykov.foodbanks.data.city.cache.CityWithPointsCache
import com.yuriisurzhykov.foodbanks.data.point.LatLng
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

private const val TestCityCode = "sac"

class CityCacheDataSourceTest {

    @Test
    fun `test searching city by code`(): Unit = runBlocking {
        val dao = TestCityDao()
        val dataSource = TestCityCacheDataSource(dao)
        assertEquals(dao.cities, dataSource.cities())
        val points = dataSource.cityByCode(TestCityCode).points
        assertEquals(dao.points, points)
    }
}

private class TestCityDao : CityDao {

    val cities = mutableListOf(
        CityCache(1, "City 1", TestCityCode, "CA", "USA")
    )

    val points = listOf(
        PointCache(
            0, TestCityCode, "3 Reg Ct", "Name", LatLng(12.1231, 32.230023), listOf(
                WorkingHour("Mon", 1, "10:00AM", "5:00PM")
            )
        )
    )

    override suspend fun cities(): List<CityCache> = cities

    override suspend fun cityWithPoints(cityCode: String): CityWithPointsCache {
        return CityWithPointsCache(cities.find { it.nameCode == cityCode }!!, points)
    }

    override suspend fun insert(city: List<CityCache>) {
        cities.addAll(city)
    }

    override suspend fun insert(city: CityCache) {
        cities.add(city)
    }

    override suspend fun delete(city: CityCache) {
        cities.remove(city)
    }

    override suspend fun update(city: CityCache) {
        val position = cities.indexOf(city)
        cities.removeAt(position)
        cities.add(position, city)
    }

    override suspend fun deleteAll() {
        cities.clear()
    }
}

private class TestCityCacheDataSource(dao: CityDao) : CityCacheDataSource.Abstract(dao)