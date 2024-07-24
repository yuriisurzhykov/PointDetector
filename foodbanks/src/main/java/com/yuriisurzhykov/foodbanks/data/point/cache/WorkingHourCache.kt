package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(value = ["pointId", "workingHourId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = PointCache::class,
            parentColumns = ["pointId"],
            childColumns = ["pointId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class WorkingHourCache(
    @ColumnInfo(name = "dayName")
    val dayName: String,
    @ColumnInfo(name = "dayValue")
    val dayValue: Int,
    @ColumnInfo(name = "hoursFrom")
    val hoursFrom: String,
    @ColumnInfo(name = "hoursTo")
    val hoursTo: String,
    @ColumnInfo(name = "pointId")
    val pointId: Long,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workingHourId")
    val workingHourId: Long = 0
)