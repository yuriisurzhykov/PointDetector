package com.yuriisurzhykov.foodbanks.data.city.cache

interface CityCacheDataSource {

    suspend fun cityByCode(codeName: String): CityWithPointsCache

    suspend fun cities(): List<CityCache>

    suspend fun insertAll(list: List<CityCache>)

    abstract class Abstract(
        private val cityDao: CityDao
    ) : CityCacheDataSource {

        override suspend fun cityByCode(codeName: String) = cityDao.cityWithPoints(codeName)

        override suspend fun cities(): List<CityCache> = cityDao.cities()

        override suspend fun insertAll(list: List<CityCache>) = cityDao.insert(list)
    }
}