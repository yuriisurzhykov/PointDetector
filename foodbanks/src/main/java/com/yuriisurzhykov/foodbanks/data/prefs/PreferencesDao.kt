package com.yuriisurzhykov.foodbanks.data.prefs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PreferencesDao {

    @Query("SELECT * FROM PreferenceCache WHERE prefName=:name")
    suspend fun preference(name: String): PreferenceCache

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pref: PreferenceCache)

    @Delete
    suspend fun delete(pref: PreferenceCache)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(pref: PreferenceCache)
}