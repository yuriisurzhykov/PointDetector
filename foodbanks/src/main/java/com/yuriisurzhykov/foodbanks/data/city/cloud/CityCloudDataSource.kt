package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.core.map
import javax.inject.Inject

interface CityCloudDataSource {

    suspend fun cities(): List<CityCloud>

    class Base @Inject constructor(
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