package com.yuriisurzhykov.pointdetector.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 2, entities = [PointCache::class])
abstract class CacheDatabase : RoomDatabase() {

    abstract fun providePointsDao(): PointsDao

}