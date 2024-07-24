package com.yuriisurzhykov.foodbanks.data.favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM FavoritePoint")
    fun favorites(): Flow<List<FavoritePointCache>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoritePointCache)

    @Delete
    suspend fun deleteFavorite(favorite: FavoritePointCache)

    @Query("DELETE FROM FavoritePoint WHERE favoriteId=:id")
    @Transaction
    suspend fun deleteFavorite(id: Long): Int
}