package com.yuriisurzhykov.foodbanks.core.data

import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Repository<Cloud : Any, Cache : Any> {

    suspend fun fetch(): Flow<RepositoryResponse<Cache>>

    abstract class Abstract<Cloud : Any, Cache : Any>(
        private val networkCheck: ConnectivityCheck,
        private val mapper: Mapper<Cloud, Cache>
    ) : Repository<Cloud, Cache> {

        protected abstract suspend fun cached(): Cache
        protected abstract suspend fun cloud(): Cloud
        protected abstract suspend fun cacheCloud(value: Cache)
        protected abstract suspend fun merge(cache: Cache, cloudMapped: Cache): Cache

        override suspend fun fetch(): Flow<RepositoryResponse<Cache>> = flow {
            val cacheItems = cached()
            emit(RepositoryResponse.Success(cacheItems))
            if (networkCheck.isNetworkAvailable()) {
                try {
                    val cloudValue = cloud().map(mapper)
                    cacheCloud(cloudValue)
                    emit(RepositoryResponse.Success(merge(cacheItems, cloudValue)))
                } catch (e: Exception) {
                    emit(RepositoryResponse.NetworkError(cacheItems))
                }
            } else {
                emit(RepositoryResponse.NoNetwork(cacheItems))
            }
        }
    }

}