package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

private const val TIMEOUT_MILLIS = 10_000

/**
 * The codes of the cities that the cloud has, and nothing else: no names and no points. The
 * list of cities is read without the points of the cities, so a change of the list is cheap
 * to learn about however many points there are.
 */
interface CityKeysCloud {

    suspend fun keys(): List<String>

    class Base @Inject constructor(
        private val database: FirebaseDatabase
    ) : CityKeysCloud {

        override suspend fun keys(): List<String> = try {
            shallow()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // The shallow request is a convenience; if it cannot be made, the SDK still can read them.
            viaSdk()
        }

        /** The database answers `{"code": true, ...}` for `?shallow=true`: only the keys, not the values. */
        private suspend fun shallow(): List<String> = withContext(Dispatchers.IO) {
            val root = database.reference.root.toString().trimEnd('/')
            val connection = URL("$root/cities.json?shallow=true").openConnection() as HttpURLConnection
            try {
                connection.connectTimeout = TIMEOUT_MILLIS
                connection.readTimeout = TIMEOUT_MILLIS
                check(connection.responseCode == HttpURLConnection.HTTP_OK) { "HTTP ${connection.responseCode}" }
                val body = connection.inputStream.bufferedReader().use { it.readText() }
                if (body.isBlank() || body.trim() == "null") emptyList() else JSONObject(body).keys().asSequence().toList()
            } finally {
                connection.disconnect()
            }
        }

        private suspend fun viaSdk(): List<String> =
            database.getReference("cities").get().await().children.mapNotNull { it.key }
    }
}
