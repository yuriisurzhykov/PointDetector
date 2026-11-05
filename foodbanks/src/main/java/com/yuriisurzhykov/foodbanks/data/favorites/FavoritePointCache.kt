package com.yuriisurzhykov.foodbanks.data.favorites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache

@Entity(
    tableName = "FavoritePoint",
    indices = [
        Index(value = ["pointId"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = PointCache::class,
            parentColumns = ["pointId"],
            childColumns = ["pointId"]
        )
    ]
)
data class FavoritePointCache(
    @PrimaryKey(autoGenerate = true)
    val favoriteId: Long,
    val pointId: Long,
    val pointName: String,
    val pointAddress: String
)
