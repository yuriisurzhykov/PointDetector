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
    fun cities(): Flow<CityCache>

    @Query("SELECT * FROM CityCache WHERE cityId=:cityId")
    @Transaction
    fun cityWithPoints(cityId: Long): Flow<CityWithPointsCache>

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