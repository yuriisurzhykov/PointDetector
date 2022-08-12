package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.PointCache
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

class CacheToUiListPointsMapper @Inject constructor(private val mapper: Mapper<PointCache, Point>) :
    Mapper<List<PointCache>, List<Point>> {

    override fun map(from: List<PointCache>): List<Point> {
        return from.map { mapper.map(it) }
    }
}