package com.yuriisurzhykov.foodbanks.data.city.cache

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index("nameCode", unique = true),
        Index("cityId", unique = true)
    ]
)
data class CityCache(
    @PrimaryKey(autoGenerate = true)
    val cityId: Long = 0,
    val name: String,
    val nameCode: String = "",
    val region: String,
    val country: String
)