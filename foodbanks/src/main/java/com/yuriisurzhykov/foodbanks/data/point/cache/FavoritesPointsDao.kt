package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesPointsDao {

    fun favorites(): Flow<List<PointCache>>
}