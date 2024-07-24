package com.yuriisurzhykov.foodbanks.data.city.cache

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "CityCache",
    indices = [
        Index(name = "nameCode", value = ["nameCode"], unique = true),
        Index(name = "cityId", value = ["cityId"], unique = true)
    ]
)
data class CityCache(
    @PrimaryKey(autoGenerate = true)
    val cityId: Long,
    val name: String,
    val nameCode: String,
    val region: String,
    val country: String
)