package com.yuriisurzhykov.foodbanks.data.favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM FavoritePoint")
    fun favorites(): Flow<List<FavoritePointCache>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: FavoritePointCache)

    @Delete
    fun deleteFavorite(favorite: FavoritePointCache)
}