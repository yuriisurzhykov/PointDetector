package com.yuriisurzhykov.pointdetector.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ConfigEntity")
data class ConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "config_name")
    val configName: String,
    val configValue: String,
    val configDataType: String
)