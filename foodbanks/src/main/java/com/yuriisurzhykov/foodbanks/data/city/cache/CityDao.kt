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

    @Transaction
    suspend fun insertOrUpdate(cities: List<CityCache>) {
        cities.forEach { city ->
            val id = cityId(city.nameCode)
            if (id == null) insert(city) else update(city.copy(cityId = id))
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
