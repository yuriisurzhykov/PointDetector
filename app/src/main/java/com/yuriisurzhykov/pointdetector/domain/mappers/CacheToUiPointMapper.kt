package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursCache
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import com.yuriisurzhykov.pointdetector.domain.usecase.CheckPointAvailabilityUseCase
import com.yuriisurzhykov.pointsdetector.uicomponents.workday.entity.WeekDay
import javax.inject.Inject

class CacheToUiPointMapper @Inject constructor(
    private val distanceCalculateService: DistanceCalculateService,
    private val userLocationService: IUserLocationService,
    private val availabilityService: CheckPointAvailabilityUseCase,
    private val workingHourToWeekDaysMapper: Mapper<List<WorkingHoursCache>, List<WeekDay>>
) : Mapper<PointCache, Point> {

    override fun map(from: PointCache): Point {
        return Point(
            from.address,
            from.coordinates,
            distanceCalculateService.calculateDistance(
                from.coordinates,
                userLocationService.currentUserLocation()
            ),
            from.placeName,
            workingHourToWeekDaysMapper.map(from.workingHoursCache.workingHours),
            availabilityService.isPointAvailableNow(from)
        )
    }
}