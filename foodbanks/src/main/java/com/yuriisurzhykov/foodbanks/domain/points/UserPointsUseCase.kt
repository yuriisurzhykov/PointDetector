package com.yuriisurzhykov.foodbanks.domain.points

import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.data.point.user.UserPoint
import com.yuriisurzhykov.foodbanks.data.point.user.UserPointsRepository
import com.yuriisurzhykov.foodbanks.data.prefs.NoPreferenceException
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import com.yuriisurzhykov.geo.LatLng
import javax.inject.Inject

/**
 * The extension point for the points added by the user. There is no UI for it yet, but a point
 * added here is shown in [PointsListUseCase] together with the cloud points of the same city.
 */
interface UserPointsUseCase {

    /**
     * Adds the point to the selected city.
     */
    @kotlin.jvm.Throws(NoPreferenceException::class)
    suspend fun add(
        placeName: String,
        address: String,
        coordinates: LatLng,
        workingHours: List<WorkingHour>
    )

    /**
     * @return true if the point was added by the user and now is removed
     */
    suspend fun remove(pointId: Long): Boolean

    abstract class Abstract(
        private val preference: SelectedCityPreference,
        private val repository: UserPointsRepository
    ) : UserPointsUseCase {

        override suspend fun add(
            placeName: String,
            address: String,
            coordinates: LatLng,
            workingHours: List<WorkingHour>
        ) {
            val cityCode = preference.getPrefValue()
            repository.add(UserPoint(cityCode, placeName, address, coordinates, workingHours))
        }

        override suspend fun remove(pointId: Long) = repository.remove(pointId)
    }

    class Base @Inject constructor(
        preference: SelectedCityPreference,
        repository: UserPointsRepository
    ) : Abstract(preference, repository)
}
