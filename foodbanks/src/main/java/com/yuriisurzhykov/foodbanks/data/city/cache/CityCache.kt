package com.yuriisurzhykov.foodbanks.data.city.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityCache(
    @PrimaryKey(autoGenerate = true)
    val cityId: Long,
    val name: String,
    val nameCode: String,
    val region: String,
    val country: String
)