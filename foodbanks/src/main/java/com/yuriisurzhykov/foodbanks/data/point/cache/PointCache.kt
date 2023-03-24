package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yuriisurzhykov.foodbanks.data.point.LatLng
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour

@Entity
data class PointCache(
    @PrimaryKey(autoGenerate = true)
    val pointId: Long,
    val address: String,
    val placeName: String,
    @Embedded
    val coordinates: LatLng,
    @Embedded
    val workingHours: List<WorkingHour>
)