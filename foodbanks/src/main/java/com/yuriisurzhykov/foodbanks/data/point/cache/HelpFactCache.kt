package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One fact about how to get help in a point, e.g. type `appointment` and `enabled = true`.
 * A point has a row only for the facts that are known: no row means "not known", never "no".
 */
@Entity(
    tableName = "HelpFactCache",
    indices = [
        Index(value = ["pointId", "type"], unique = true)
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
data class HelpFactCache(
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "enabled")
    val enabled: Boolean,
    @ColumnInfo(name = "pointId")
    val pointId: Long,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "helpFactId")
    val helpFactId: Long = 0
)
