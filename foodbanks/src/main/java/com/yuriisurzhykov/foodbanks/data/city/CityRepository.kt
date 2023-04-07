package com.yuriisurzhykov.foodbanks.data.city

import com.yuriisurzhykov.foodbanks.core.data.Repository
import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cache.CityDao
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloud
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloudDataSource
import javax.inject.Inject

interface CityRepository : Repository<List<CityCloud>, List<CityCache>> {

    class Base @Inject constructor(
        private val cityCloudDataSource: CityCloudDataSource,
        private val cityCacheDataSource: CityDao,
        cloudToCacheMapper: CloudToCacheListMapper,
        connectivityCheck: ConnectivityCheck,
    ) : Repository.Abstract<List<CityCloud>, List<CityCache>>(
        connectivityCheck,
        cloudToCacheMapper
    ), CityRepository {

        override suspend fun cached(): List<CityCache> = cityCacheDataSource.cities()

        override suspend fun cloud(): List<CityCloud> = cityCloudDataSource.cities()

        override suspend fun cacheCloud(value: List<CityCache>) =
            cityCacheDataSource.insert(value)
    }
}