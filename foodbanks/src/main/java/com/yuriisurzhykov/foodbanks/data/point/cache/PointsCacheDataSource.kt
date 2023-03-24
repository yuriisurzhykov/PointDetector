package com.yuriisurzhykov.foodbanks.data.point.cache

import kotlinx.coroutines.flow.Flow

interface PointsCacheDataSource {
    fun points(): Flow<PointCache>
}