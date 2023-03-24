package com.yuriisurzhykov.foodbanks.data.city.cache

import kotlinx.coroutines.flow.Flow

interface CityCacheDataSource {
    fun cities(): Flow<CityCache>
}