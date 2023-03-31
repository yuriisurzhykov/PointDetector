package com.yuriisurzhykov.foodbanks.data.point.cloud

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.core.data.map
import com.yuriisurzhykov.foodbanks.data.RemoteDataException
import javax.inject.Inject

interface PointCloudDataSource {

    suspend fun getPoints(cityId: String): List<PointCloud>

    class Base @Inject constructor(
        private val database: FirebaseDatabase,
        private val snapshotMapper: SnapshotPointMapper
    ) : PointCloudDataSource {
        override suspend fun getPoints(cityId: String): List<PointCloud> {
            val task = database.getReference("cities")
                .child(cityId)
                .get()
            if (task.isSuccessful && !task.isCanceled) {
                return task.result.children.map { it.map(snapshotMapper) }
            } else throw RemoteDataException(task.exception!!)
        }
    }
}