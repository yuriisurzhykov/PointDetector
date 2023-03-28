package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PointsDao {

    @Query("SELECT * FROM PointCache WHERE cityCodeId=:cityId")
    fun pointsByCity(cityId: String): List<PointCache>

    @Insert
    suspend fun insert(point: PointCache)

    @Insert
    suspend fun insert(point: List<PointCache>)

    @Delete
    suspend fun delete(point: PointCache)

    @Update
    suspend fun update(point: PointCache)
}