package com.yuriisurzhykov.pointdetector.data.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PointCache(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val address: String,
    @Embedded
    val coordinates: LatLng
)