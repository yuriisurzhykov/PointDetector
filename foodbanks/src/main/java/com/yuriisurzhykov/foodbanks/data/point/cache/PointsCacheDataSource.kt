package com.yuriisurzhykov.foodbanks.data.point.cache

import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PointsCacheDataSource {

    suspend fun points(): Flow<PointCache>

    abstract class Abstract(
        private val cityPreference: SelectedCityPreference, private val pointsDao: PointsDao
    ) : PointsCacheDataSource {

        override suspend fun points() = pointsDao.pointsByCity(cityPreference.getPrefValue())
    }

    class Base @Inject constructor(
        cityPreference: SelectedCityPreference,
        pointsDao: PointsDao
    ) : Abstract(cityPreference, pointsDao)
}