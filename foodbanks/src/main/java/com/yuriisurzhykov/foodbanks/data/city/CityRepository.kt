package com.yuriisurzhykov.foodbanks.data.city

import com.yuriisurzhykov.foodbanks.core.data.Delete
import com.yuriisurzhykov.foodbanks.core.data.Save
import com.yuriisurzhykov.foodbanks.core.map
import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck
import com.yuriisurzhykov.foodbanks.data.RemoteDataException
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloudDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CityRepository : Save<CityCache>, Delete<CityCache> {

    suspend fun cities(): Flow<CityRepositoryResponse>

    abstract class Abstract(
        private val cloudToCacheMapper: CloudToCacheListMapper,
        private val cityCloudDataSource: CityCloudDataSource,
        private val cityCacheDataSource: CityCacheDataSource,
        private val connectivityCheck: ConnectivityCheck,
    ) : CityRepository {

        override suspend fun cities() = flow {
            val cacheList = cityCacheDataSource.cities()
            emit(CityRepositoryResponse.Success(cacheList))
            if (connectivityCheck.isNetworkAvailable()) {
                try {
                    val cloud = cityCloudDataSource.cities()
                    val cache = cloud.map(cloudToCacheMapper)
                    cityCacheDataSource.insertAll(cache)
                    emit(CityRepositoryResponse.Success(cityCacheDataSource.cities()))
                } catch (e: RemoteDataException) {
                    emit(CityRepositoryResponse.ServerFailed(e, cacheList))
                }
            } else {
                emit(CityRepositoryResponse.NoInternetConnection(cacheList))
            }
        }
    }

}