package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.PointCache
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import javax.inject.Inject

class CacheToUiPointMapper @Inject constructor(
    private val distanceCalculateService: DistanceCalculateService,
    private val userLocationService: IUserLocationService
) : Mapper<PointCache, Point> {

    override fun map(from: PointCache): Point {
        return Point(
            from.address,
            from.coordinates,
            distanceCalculateService.calculateDistance(
                from.coordinates,
                userLocationService.currentUserLocation()
            )
        )
    }
}