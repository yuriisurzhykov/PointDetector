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

    /**
     * The cache gives the points of all sources combined (cloud and the ones added by the
     * user), while the cloud is the source of only one of them, so only that one is synced.
     */
    class Base @Inject constructor(
        private val pointDao: PointsDao,
        private val pointsSource: PointsSource,
        private val selectedCityPreference: SelectedCityPreference,
        private val pointCloudDataSource: PointCloudDataSource,
        cloudCacheMapper: CloudToCachePointMapper,
        networkCheck: ConnectivityCheck
    ) : Repository.Abstract<List<PointCloud>, List<PointWithHours>>(networkCheck, cloudCacheMapper),
        PointsRepository {

        override suspend fun cached(): List<PointWithHours> {
            val city = selectedCityPreference.getPrefValue()
            return pointsSource.points(city)
        }

        override suspend fun cloud(): List<PointCloud> {
            val city = selectedCityPreference.getPrefValue()
            return pointCloudDataSource.getPoints(city)
        }

        override suspend fun cacheCloud(value: List<PointWithHours>) {
            value.forEach { pointDao.insertOrUpdate(it) }
        }

        /**
         * The cache has just been updated with the cloud values, so reading it back gives the
         * merged result together with the generated ids that cloud values don't have yet.
         */
        override suspend fun merge(
            cache: List<PointWithHours>,
            cloudMapped: List<PointWithHours>
        ): List<PointWithHours> = cached()
    }
}
