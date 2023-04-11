package com.yuriisurzhykov.foodbanks.data.sync.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["syncEntity", "id"])
data class SyncPropertyCache(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val lastSyncTime: Long,
    val syncEntity: String
)