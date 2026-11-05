package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.yuriisurzhykov.foodbanks.core.data.SnapshotValue
import com.yuriisurzhykov.foodbanks.core.data.map
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Reads the list of the cities of the cloud: the codes first, then what every city says about
 * itself, with no points. A city that cannot be read is left out, so one bad entry does not
 * hide the other cities; only a list that cannot be read at all is an error.
 */
class CityListReader(
    private val keys: CityKeysCloud,
    private val meta: CityMetaCloud,
    private val valueMapper: ValueCityMapper,
) {

    suspend fun cities(): List<CityCloud> {
        val codes = keys.keys()
        return coroutineScope { codes.map { code -> async { cityOf(code) } }.awaitAll() }.filterNotNull()
    }

    private suspend fun cityOf(code: String): CityCloud? = try {
        SnapshotValue.Base(meta.meta(code)).map(valueMapper)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        null
    }
}
