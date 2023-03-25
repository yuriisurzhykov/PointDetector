package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.point.LatLng
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour

@Entity(
    tableName = "PointCache",
    foreignKeys = [ForeignKey(
        entity = CityCache::class,
        parentColumns = ["cityId"],
        childColumns = ["cityPointId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PointCache(
    @PrimaryKey(autoGenerate = true)
    val pointId: Long,
    val cityPointId: Long,
    val address: String,
    val placeName: String,
    @Embedded
    val coordinates: LatLng,
    @Embedded
    val workingHours: List<WorkingHour>
)