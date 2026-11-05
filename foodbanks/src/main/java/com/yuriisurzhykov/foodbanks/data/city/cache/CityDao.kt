package com.yuriisurzhykov.foodbanks.data.city.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface CityDao {

    @Query("SELECT * FROM CityCache")
    suspend fun cities(): List<CityCache>

    @Query("SELECT * FROM CityCache WHERE nameCode=:cityCode")
    suspend fun city(cityCode: String): CityCache?

    @Query("SELECT * FROM CityCache WHERE nameCode=:cityCode")
    @Transaction
    suspend fun cityWithPoints(cityCode: String): CityWithPointsCache

    @Query("SELECT cityId FROM CityCache WHERE nameCode=:cityCode")
    suspend fun cityId(cityCode: String): Long?

    @Insert
    suspend fun insert(city: List<CityCache>)

    @Insert
    suspend fun insert(city: CityCache)

    /**
     * A new city is added, a known one (the same code) is updated. The list of cities comes
     * without the points, so a city that has no coordinates in the cloud has no center there;
     * the center that was worked out earlier is kept instead of being erased.
     */
    @Transaction
    suspend fun insertOrUpdate(cities: List<CityCache>) {
        cities.forEach { city ->
            val known = city(city.nameCode)
            if (known == null) {
                insert(city)
            } else {
                update(
                    city.copy(
                        cityId = known.cityId,
                        latitude = city.latitude ?: known.latitude,
                        longitude = city.longitude ?: known.longitude,
                    ),
                )
            }
        }
    }

    @Delete
    suspend fun delete(city: CityCache)

    @Update
    suspend fun update(city: CityCache)

    @Query("DELETE FROM CityCache")
    @Transaction
    suspend fun deleteAll()
}
