package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private val CITY_FIELDS = listOf("name", "name_code", "region", "country", "coordinates")

/** What a city says about itself without its points: the fields of the city, read one by one. */
interface CityMetaCloud {

    /** The fields of the city that exist, as the database gives them (a map for the coordinates). */
    suspend fun meta(code: String): Map<String, Any?>

    class Base @Inject constructor(
        private val database: FirebaseDatabase
    ) : CityMetaCloud {

        override suspend fun meta(code: String): Map<String, Any?> = coroutineScope {
            val city = database.getReference("cities").child(code)
            CITY_FIELDS
                .map { field -> field to async { city.child(field).get().await().value } }
                .mapNotNull { (field, value) -> value.await()?.let { field to it } }
                .toMap()
        }
    }
}
