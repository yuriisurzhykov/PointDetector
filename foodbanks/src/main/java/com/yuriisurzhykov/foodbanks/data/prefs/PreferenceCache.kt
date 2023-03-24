package com.yuriisurzhykov.foodbanks.data.prefs

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PreferenceCache(
    @PrimaryKey(autoGenerate = true)
    val entityId: Long,
    val prefName: String,
    val prefValue: String
)