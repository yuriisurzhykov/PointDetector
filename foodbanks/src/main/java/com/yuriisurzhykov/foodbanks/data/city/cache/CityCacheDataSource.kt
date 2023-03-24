package com.yuriisurzhykov.foodbanks.data.city.cache

import kotlinx.coroutines.flow.Flow

interface CityCacheDataSource {

    fun cities(): Flow<CityCache>

    abstract class Abstract(
        private val cityDao: CityDao
    ) : CityCacheDataSource {
        override fun cities(): Flow<CityCache> = cityDao.cities()
    }
}