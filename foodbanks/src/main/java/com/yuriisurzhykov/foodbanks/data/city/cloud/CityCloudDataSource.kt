package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.core.data.map
import com.yuriisurzhykov.foodbanks.data.RemoteDataException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CityCloudDataSource {

    /**
     * The cities of the cloud **without their points**. A city that cannot be read is left out,
     * so one bad entry does not hide the others; the failure is thrown only when the list itself
     * cannot be read.
     */
    @kotlin.jvm.Throws(RemoteDataException::class)
    suspend fun cities(): List<CityCloud>

    /** One city with its points. Points that cannot be read are left out. */
    @kotlin.jvm.Throws(RemoteDataException::class)
    suspend fun city(cityCode: String): CityCloud

    class Base @Inject constructor(
        private val database: FirebaseDatabase,
        private val snapshotMapper: SnapshotCityMapper,
        private val list: CityListReader,
    ) : CityCloudDataSource {

        override suspend fun cities(): List<CityCloud> = remote { list.cities() }

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
