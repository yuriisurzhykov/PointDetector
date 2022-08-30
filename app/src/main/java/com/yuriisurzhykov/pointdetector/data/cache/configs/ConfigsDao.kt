package com.yuriisurzhykov.pointdetector.data.cache.configs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yuriisurzhykov.pointdetector.data.cache.entities.ConfigEntity

@Dao
interface ConfigsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ConfigEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: ConfigEntity)

    @Delete
    suspend fun delete(entity: ConfigEntity)

    @Query("SELECT * FROM configentity WHERE config_name=:configName")
    suspend fun findConfig(configName: String): ConfigEntity

    @Query("SELECT * FROM configentity")
    fun fetchAllConfigs(): LiveData<List<ConfigEntity>>

}