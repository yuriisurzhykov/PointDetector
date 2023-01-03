package com.yuriisurzhykov.pointdetector.domain.mappers

import android.content.Context
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Distance
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import com.yuriisurzhykov.pointdetector.domain.usecase.CheckPointAvailabilityUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.ConfigUnifiedSource
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PointToPointsUiMapper @Inject constructor(
    private val distanceCalculateService: DistanceCalculateService,
    private val userLocationService: IUserLocationService,
    private val configUnifiedSource: ConfigUnifiedSource,
    private val availabilityService: CheckPointAvailabilityUseCase.DomainPoint,
    @ApplicationContext private val appContext: Context
) : SuspendMapper<Point, PointUi> {
    override suspend fun map(from: Point): PointUi {
        val isMiles = configUnifiedSource.measureConfigValue() == "miles"
        val distance = distanceCalculateService.calculateDistance(
            from.coordinates,
            userLocationService.currentUserLocation()
        )
        val distanceMeasure =
            if (isMiles) Distance.Miles(distance) else Distance.Kilometers(distance)
        return PointUi(
            from.address,
            from.coordinates,
            distanceMeasure.getDistanceLocale(appContext),
            distance,
            from.placeName,
            from.workingHours,
            availabilityService.isPointAvailableNow(from)
        )
    }
}