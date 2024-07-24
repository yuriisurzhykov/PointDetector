package com.yuriisurzhykov.foodbanks.data.point

import com.yuriisurzhykov.foodbanks.core.data.Repository
import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck
import com.yuriisurzhykov.foodbanks.data.point.cache.PointWithHours
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsDao
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloudDataSource
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import javax.inject.Inject

interface PointsRepository : Repository<List<PointCloud>, List<PointWithHours>> {

    class Base @Inject constructor(
        private val pointDao: PointsDao,
        private val selectedCityPreference: SelectedCityPreference,
        private val pointCloudDataSource: PointCloudDataSource,
        cloudCacheMapper: CloudToCachePointMapper,
        networkCheck: ConnectivityCheck
    ) : Repository.Abstract<List<PointCloud>, List<PointWithHours>>(networkCheck, cloudCacheMapper),
        PointsRepository {

        override suspend fun cached(): List<PointWithHours> {
            val city = selectedCityPreference.getPrefValue()
            return pointDao.pointsByCity(city)
        }

        override suspend fun cloud(): List<PointCloud> {
            val city = selectedCityPreference.getPrefValue()
            return pointCloudDataSource.getPoints(city)
        }

        override suspend fun cacheCloud(value: List<PointWithHours>) {
            value.forEach { pointDao.insert(it) }
        }

        override suspend fun merge(
            cache: List<PointWithHours>,
            cloudMapped: List<PointWithHours>
        ): List<PointWithHours> {
            return (cache.toSet() + cloudMapped.toSet()).toSet().toList()
        }
    }
}