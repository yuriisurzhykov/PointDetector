package com.yuriisurzhykov.pointdetector.data.cache

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yuriisurzhykov.pointdetector.data.cache.entities.PointCache
import kotlinx.coroutines.flow.Flow

@Dao
interface PointsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PointCache)

    @Delete
    suspend fun delete(entity: PointCache)

    @Query("UPDATE PointCache SET isFavorite=:isFavorite WHERE id=:entityId")
    suspend fun update(entityId: Long, isFavorite: Boolean)

    @Query("DELETE FROM PointCache")
    suspend fun clearAll()

    @Query("SELECT * FROM pointcache")
    fun fetchAll(): Flow<List<PointCache>>

    @Query("SELECT * FROM PointCache WHERE address LIKE '%' || :value || '%' OR placeName LIKE '%' || :value || '%'")
    fun fetchByPlaceContains(value: String): Flow<List<PointCache>>

    @Query("DELETE FROM PointCache WHERE address=:addressValue AND placeName=:name")
    suspend fun deleteByAddressAndName(addressValue: String, name: String)

    @Query("SELECT * FROM PointCache WHERE isFavorite=='1'")
    fun fetchFavorites(): Flow<List<PointCache>>

}