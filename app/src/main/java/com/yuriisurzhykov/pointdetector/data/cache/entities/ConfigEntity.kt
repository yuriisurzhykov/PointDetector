package com.yuriisurzhykov.pointdetector.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ConfigEntity")
data class ConfigEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "config_name")
    val configName: String,
    val configValue: String,
    val configDataType: String
)