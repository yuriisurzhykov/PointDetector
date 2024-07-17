package com.yuriisurzhykov.pointdetector.domain.mappers

import android.content.Context
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursCache
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Distance
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import com.yuriisurzhykov.pointdetector.domain.usecase.CheckPointAvailabilityUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.ConfigUnifiedSource
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CacheToUiPointMapper @Inject constructor(
    private val distanceCalculateService: DistanceCalculateService,
    private val userLocationService: IUserLocationService,
    private val configUnifiedSource: ConfigUnifiedSource,
    private val availabilityService: CheckPointAvailabilityUseCase.CachePoint,
    private val workingHourToWeekDaysMapper: Mapper<List<WorkingHoursCache>, List<WeekDay>>,
    @ApplicationContext private val appContext: Context
) : SuspendMapper<PointCache, PointUi> {

    override suspend fun map(from: PointCache): PointUi {
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
            workingHourToWeekDaysMapper.map(from.workingHoursCache.workingHours),
            availabilityService.isPointAvailableNow(from),
            FavoriteState.Factory.build(from.isFavorite)
        )
    }
}