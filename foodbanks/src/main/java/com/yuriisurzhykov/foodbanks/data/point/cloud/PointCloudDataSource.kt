package com.yuriisurzhykov.foodbanks.data.point.cloud

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.core.map
import javax.inject.Inject

interface PointCloudDataSource {

    suspend fun getPoints(cityId: String): List<PointCloud>

    class Base @Inject constructor(
        private val database: FirebaseDatabase,
        private val snapshotMapper: SnapshotPointMapper
    ) : PointCloudDataSource {
        override suspend fun getPoints(cityId: String): List<PointCloud> {
            return database.getReference("cities")
                .child(cityId)
                .get()
                .result
                .children.map { child -> child.map(snapshotMapper) }
        }
    }
}