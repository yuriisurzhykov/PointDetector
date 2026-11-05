package com.yuriisurzhykov.pointdetector.domain.mappers

import android.content.Context
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.data.point.cache.PointOrigin
import com.yuriisurzhykov.foodbanks.domain.points.Point
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Distance
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import com.yuriisurzhykov.pointdetector.domain.services.WeekDaysListResource
import com.yuriisurzhykov.pointdetector.domain.usecase.CheckPointAvailabilityUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.ConfigUnifiedSource
import com.yuriisurzhykov.pointdetector.presentation.entities.FavoriteState
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Makes a point of the city (a cloud one or the one added by the user) ready to be shown:
 * adds the distance to the user, the availability right now and the days as the UI needs them.
 */
class PointToUiMapper @Inject constructor(
    private val distanceCalculateService: DistanceCalculateService,
    private val userLocationService: IUserLocationService,
    private val configUnifiedSource: ConfigUnifiedSource,
    private val availabilityService: CheckPointAvailabilityUseCase.WorkingHours,
    private val weekDaysListResource: WeekDaysListResource,
    @ApplicationContext private val appContext: Context
) : SuspendMapper<Point, PointUi> {

    override suspend fun map(from: Point): PointUi {
        val point = from.map(PointFields)
        val coordinates = LatLng(point.latitude, point.longitude)
        val isMiles = configUnifiedSource.measureConfigValue() == "miles"
        val distance = distanceCalculateService.calculateDistance(
            coordinates,
            userLocationService.currentUserLocation()
        )
        val distanceMeasure =
            if (isMiles) Distance.Miles(distance) else Distance.Kilometers(distance)
        return PointUi(
            address = point.address,
            coordinates = coordinates,
            distanceString = distanceMeasure.getDistanceLocale(appContext),
            distanceDouble = distance,
            placeName = point.name,
            workingHours = point.workingHours.map { weekDay(it) },
            isPointAvailable = availabilityService.isPointAvailableNow(point.workingHours),
            favoriteState = FavoriteState.Factory.build(point.favorite),
            id = point.id,
            isUserPoint = point.origin == PointOrigin.USER
        )
    }

    private fun weekDay(hour: WorkingHour) = WeekDay(
        hour.dayName,
        weekDaysListResource.getDaysAsList().indexOf(hour.dayName),
        hour.hoursFrom,
        hour.hoursTo
    )

    private class Fields(
        val id: Long,
        val name: String,
        val address: String,
        val latitude: Double,
        val longitude: Double,
        val workingHours: List<WorkingHour>,
        val origin: PointOrigin,
        val favorite: Boolean
    )

    private object PointFields : Point.Mapper<Fields> {

        override fun map(
            id: Long,
            name: String,
            address: String,
            coordinates: com.yuriisurzhykov.geo.LatLng,
            workingHours: List<WorkingHour>,
            origin: PointOrigin,
            favorite: Boolean
        ) = Fields(
            id, name, address, coordinates.lat, coordinates.lng, workingHours, origin, favorite
        )
    }
}
