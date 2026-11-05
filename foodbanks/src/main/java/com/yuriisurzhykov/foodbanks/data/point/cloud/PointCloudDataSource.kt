package com.yuriisurzhykov.foodbanks.data.point.cloud

import com.yuriisurzhykov.foodbanks.data.RemoteDataException
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloudDataSource
import javax.inject.Inject

interface PointCloudDataSource {

    @kotlin.jvm.Throws(RemoteDataException::class)
    suspend fun getPoints(cityId: String): List<PointCloud>

    class Base @Inject constructor(
        private val cityCloudDataSource: CityCloudDataSource
    ) : PointCloudDataSource {

        override suspend fun getPoints(cityId: String): List<PointCloud> {
            return cityCloudDataSource.city(cityId).points
        }
    }
}
