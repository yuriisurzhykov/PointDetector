package com.yuriisurzhykov.foodbanks.data.city

import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache

interface CityRepositoryResponse {

    class Success(private val cities: List<CityCache>) : CityRepositoryResponse

    class NoInternetConnection(private val cacheList: List<CityCache>) : CityRepositoryResponse

    class ServerFailed(
        private val exception: java.lang.Exception,
        private val cacheList: List<CityCache>
    ) : CityRepositoryResponse

    class NoCacheData : CityRepositoryResponse
}