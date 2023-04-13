package com.yuriisurzhykov.foodbanks.data.sync

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index("syncEntity", unique = true)]
)
data class SyncProperty(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val lastSyncTime: Long,
    val syncEntity: String
)