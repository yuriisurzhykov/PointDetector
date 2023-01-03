package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import javax.inject.Inject

class CacheToUiListPointsMapper @Inject constructor(private val mapper: SuspendMapper<PointCache, PointUi>) :
    SuspendMapper<List<PointCache>, List<PointUi>> {

    override suspend fun map(from: List<PointCache>): List<PointUi> {
        return from.map { mapper.map(it) }
    }
}