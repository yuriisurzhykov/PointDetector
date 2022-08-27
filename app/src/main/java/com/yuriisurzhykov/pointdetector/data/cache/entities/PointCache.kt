package com.yuriisurzhykov.pointdetector.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PointCache(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val address: String,
    @Embedded
    val coordinates: LatLng,
    @ColumnInfo(defaultValue = "")
    val placeName: String,
    @ColumnInfo(defaultValue = "")
    val workingHoursCache: WorkingHoursGroupCache = WorkingHoursGroupCache(emptyList())
)