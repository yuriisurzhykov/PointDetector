package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface PointsDao {

    @Query("SELECT * FROM PointCache WHERE cityCodeId=:cityId")
    @Transaction
    suspend fun pointsByCity(cityId: String): List<PointWithHours>

    @Query("SELECT * FROM PointCache WHERE pointId=:id")
    suspend fun pointById(id: Long): PointCache

    @Query("SELECT pointId FROM PointCache WHERE cityCodeId=:cityId AND address=:address")
    suspend fun pointId(cityId: String, address: String): Long?

    @Query("DELETE FROM WorkingHourCache WHERE pointId=:pointId")
    suspend fun deleteWorkingHours(pointId: Long)

    @Insert
    suspend fun insert(point: PointCache): Long

    @Insert
    suspend fun insert(workingHourCache: WorkingHourCache)

    @Transaction
    suspend fun insert(point: PointWithHours) {
        val id = insert(point.point)
        point.workingHours.forEach {
            insert(it.copy(pointId = id))
        }
    }

    @Transaction
    suspend fun insertOrUpdate(point: PointWithHours) {
        val id = pointId(point.point.cityCodeId, point.point.address)
        if (id == null) {
            insert(point)
        } else {
            update(point.point.copy(pointId = id))
            deleteWorkingHours(id)
            point.workingHours.forEach { insert(it.copy(pointId = id)) }
        }
    }

    @Insert
    suspend fun insert(point: List<PointCache>)

    @Delete
    suspend fun delete(point: PointCache)

    @Update
    suspend fun update(point: PointCache)
}
