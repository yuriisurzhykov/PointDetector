package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.point.LatLng

@Entity(
    tableName = "PointCache",
    indices = [
        Index(value = ["cityCodeId", "pointId"])
    ],
    foreignKeys = [ForeignKey(
        entity = CityCache::class,
        parentColumns = ["nameCode"],
        childColumns = ["cityCodeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PointCache(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pointId")
    val pointId: Long,
    @ColumnInfo(name = "cityCodeId")
    val cityCodeId: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "placeName")
    val placeName: String,
    @Embedded
    var coordinates: LatLng = LatLng(0.0, 0.0),
)