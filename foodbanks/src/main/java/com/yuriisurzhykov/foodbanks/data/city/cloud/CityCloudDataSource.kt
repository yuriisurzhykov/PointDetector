package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.core.data.map
import com.yuriisurzhykov.foodbanks.data.RemoteDataException
import javax.inject.Inject

interface CityCloudDataSource {

    @kotlin.jvm.Throws(RemoteDataException::class)
    suspend fun cities(): List<CityCloud>

    class Base @Inject constructor(
        private val database: FirebaseDatabase,
        private val snapshotMapper: SnapshotCityMapper
    ) : CityCloudDataSource {

        override suspend fun cities(): List<CityCloud> {
            val task = database.getReference("cities").get()
            if (task.isSuccessful) {
                return task.result
                    .children.map { child -> child.map(snapshotMapper) }
            } else {
                throw RemoteDataException(task.exception!!)
            }
        }
    }
}