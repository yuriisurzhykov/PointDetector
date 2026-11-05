package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.core.data.map
import com.yuriisurzhykov.foodbanks.data.RemoteDataException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CityCloudDataSource {

    @kotlin.jvm.Throws(RemoteDataException::class)
    suspend fun cities(): List<CityCloud>

    @kotlin.jvm.Throws(RemoteDataException::class)
    suspend fun city(cityCode: String): CityCloud

    class Base @Inject constructor(
        private val database: FirebaseDatabase,
        private val snapshotMapper: SnapshotCityMapper
    ) : CityCloudDataSource {

        override suspend fun cities(): List<CityCloud> = remote {
            val snapshot = citiesReference().get().await()
            snapshot.children.map { child -> child.map(snapshotMapper) }
        }

        override suspend fun city(cityCode: String): CityCloud = remote {
            citiesReference().child(cityCode).get().await().map(snapshotMapper)
        }

        private fun citiesReference(): DatabaseReference = database.getReference("cities")

        private suspend fun <T> remote(request: suspend () -> T): T {
            try {
                return request()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                throw RemoteDataException(e)
            }
        }
    }
}
