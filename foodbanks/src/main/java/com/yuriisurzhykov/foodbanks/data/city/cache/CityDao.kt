package com.yuriisurzhykov.foodbanks.data.city.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM CityCache")
    suspend fun cities(): List<CityCache>

    @Query("SELECT * FROM CityCache WHERE nameCode=:cityCode")
    @Transaction
    suspend fun cityWithPoints(cityCode: String): CityWithPointsCache

    @Insert
    suspend fun insert(city: List<CityCache>)

    @Insert
    suspend fun insert(city: CityCache)

    @Delete
    suspend fun delete(city: CityCache)

    @Update
    suspend fun update(city: CityCache)

    @Query("DELETE FROM CityCache")
    @Transaction
    suspend fun deleteAll()
}