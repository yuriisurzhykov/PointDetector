package com.yuriisurzhykov.foodbanks.data.sync.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.yuriisurzhykov.foodbanks.data.sync.SyncProperty

@Dao
interface SyncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SyncProperty)

    suspend fun update()

    @Query(
                "SELECT S.id, S.lastSyncTime,S.syncEntity FROM SyncProperty AS S " +
                "INNER JOIN PreferenceCache AS P " +
                "ON s.syncEntity=P.prefValue AND P.prefName='preference_selected_city'"
    )
    @Transaction
    suspend fun lastUpdate(): SyncProperty

}