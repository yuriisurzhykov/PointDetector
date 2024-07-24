package com.yuriisurzhykov.foodbanks.data.point.cache

import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface PointsCacheDataSource {

    suspend fun points(): Flow<List<PointWithHours>>

    suspend fun findById(id: Long): PointCache

    abstract class Abstract(
        private val cityPreference: SelectedCityPreference,
        private val pointsDao: PointsDao
    ) : PointsCacheDataSource {

        override suspend fun points() = flow {
            emit(pointsDao.pointsByCity(cityPreference.getPrefValue()))
        }

        override suspend fun findById(id: Long): PointCache {
            return pointsDao.pointById(id)
        }
    }

    class Base @Inject constructor(
        cityPreference: SelectedCityPreference,
        pointsDao: PointsDao
    ) : Abstract(cityPreference, pointsDao)
}