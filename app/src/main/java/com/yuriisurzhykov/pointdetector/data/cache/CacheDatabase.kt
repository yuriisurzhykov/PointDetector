package com.yuriisurzhykov.pointdetector.data.cache

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.yuriisurzhykov.pointdetector.data.cache.configs.ConfigsDao
import com.yuriisurzhykov.pointdetector.data.cache.entities.ConfigEntity
import com.yuriisurzhykov.pointdetector.data.cache.migrations.MigrationV3ToV4Spec

@Database(
    version = 4,
    entities = [ConfigEntity::class],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4, spec = MigrationV3ToV4Spec::class)
    ]
)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun provideConfigsDao(): ConfigsDao

}
