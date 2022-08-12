package com.yuriisurzhykov.pointdetector.data.cache

import androidx.room.*

@Dao
interface PointsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PointCache)

    @Delete
    suspend fun delete(entity: PointCache)

    @Query("DELETE FROM PointCache")
    suspend fun clearAll()

    @Query("SELECT * FROM pointcache")
    suspend fun fetchAll(): List<PointCache>

    @Query("SELECT * FROM PointCache WHERE address LIKE :value")
    suspend fun fetchByPlaceContains(value: String): List<PointCache>

}