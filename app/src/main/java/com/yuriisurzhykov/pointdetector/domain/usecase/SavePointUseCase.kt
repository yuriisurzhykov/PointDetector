package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.domain.city.CityNotDetectedException
import com.yuriisurzhykov.foodbanks.domain.city.SelectedCityUseCase
import com.yuriisurzhykov.foodbanks.domain.points.UserPointsUseCase
import com.yuriisurzhykov.geo.LatLng
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

interface SavePointUseCase {

    /**
     * Saves the point as the user's own point of the selected city, so it is shown
     * together with the points from the cloud.
     */
    @kotlin.jvm.Throws(CityNotDetectedException::class)
    suspend fun save(point: Point)

    open class Base @Inject constructor(
        private val selectedCityUseCase: SelectedCityUseCase,
        private val userPointsUseCase: UserPointsUseCase
    ) : SavePointUseCase {

        override suspend fun save(point: Point) {
            selectedCityUseCase.selectedCity()
            userPointsUseCase.add(
                placeName = point.placeName,
                address = point.address,
                coordinates = LatLng(point.coordinates.lat, point.coordinates.lng),
                workingHours = point.workingHours.map {
                    WorkingHour(it.dayName, it.dayValue, it.hoursFrom, it.hoursTo)
                }
            )
        }
    }

}
