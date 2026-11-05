package com.yuriisurzhykov.foodbanks.data.prefs

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["prefName"], unique = true)])
data class PreferenceCache(
    @PrimaryKey(autoGenerate = true)
    val entityId: Long,
    val prefName: String,
    val prefValue: String
)