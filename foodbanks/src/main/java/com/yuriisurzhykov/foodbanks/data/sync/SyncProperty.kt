package com.yuriisurzhykov.foodbanks.data.sync

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["syncEntity", "id"])
data class SyncProperty(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val lastSyncTime: Long,
    val syncEntity: String
)