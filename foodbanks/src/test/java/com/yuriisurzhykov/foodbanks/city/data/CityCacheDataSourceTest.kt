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
import org.junit.Before
import org.junit.Test

private const val TestCityCode = "sac"

class CityCacheDataSourceTest {

    private lateinit var dao: TestCityDao
    private lateinit var dataSource: CityCacheDataSource

    @Before
    fun `init resources for test`() {
        dao = TestCityDao()
        dataSource = TestCityCacheDataSource(dao)
    }

    @Test
    fun `test searching city by code`(): Unit = runBlocking {
        val points = dataSource.cityByCode(TestCityCode).points
        assertEquals(dao.points, points)
    }

    @Test
    fun `test comparing all cities in data source list`(): Unit = runBlocking {
        assertEquals(dao.cities, dataSource.cities())
    }

    @Test
    fun `test inserting cities to data source`(): Unit = runBlocking {
        dataSource.insertAll(
            listOf(CityCache(0, "City 2", "sac-2", "CA", "USA"))
        )
        assertEquals(dao.cities, dataSource.cities())
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