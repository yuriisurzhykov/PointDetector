package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PointsDao {

    @Query("SELECT * FROM PointCache WHERE cityPointId=:cityId")
    fun pointsByCity(cityId: Long): Flow<PointCache>

    @Insert
    suspend fun insert(point: PointCache)

    @Delete
    suspend fun delete(point: PointCache)

    @Update
    suspend fun update(point: PointCache)
}