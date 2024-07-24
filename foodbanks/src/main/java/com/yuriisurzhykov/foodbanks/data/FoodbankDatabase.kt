package com.yuriisurzhykov.foodbanks.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cache.CityDao
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritePointCache
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesDao
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsDao
import com.yuriisurzhykov.foodbanks.data.point.cache.WorkingHourCache
import com.yuriisurzhykov.foodbanks.data.prefs.PreferenceCache
import com.yuriisurzhykov.foodbanks.data.prefs.PreferencesDao
import com.yuriisurzhykov.foodbanks.data.sync.SyncProperty
import com.yuriisurzhykov.foodbanks.data.sync.cache.SyncDao

@Database(
    entities = [
        CityCache::class,
        PointCache::class,
        PreferenceCache::class,
        SyncProperty::class,
        WorkingHourCache::class,
        FavoritePointCache::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class FoodbankDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    abstract fun pointsDao(): PointsDao

    abstract fun preferenceDao(): PreferencesDao

    abstract fun syncDao(): SyncDao

    abstract fun favoritesDao(): FavoritesDao
}