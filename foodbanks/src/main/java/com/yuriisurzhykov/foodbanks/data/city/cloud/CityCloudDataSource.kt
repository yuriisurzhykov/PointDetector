package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.core.map
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloudDataSource
import com.yuriisurzhykov.foodbanks.data.point.cloud.SnapshotPointMapper

interface CityCloudDataSource {

    suspend fun cities(): List<CityCloud>

    class Base(
        private val database: FirebaseDatabase,
        private val snapshotMapper: SnapshotCityMapper
    ) : CityCloudDataSource {

        override suspend fun cities(): List<CityCloud> {
            return database.getReference("cities")
                .get()
                .result
                .children.map { child -> child.map(snapshotMapper) }
        }
    }
}