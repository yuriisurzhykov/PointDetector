package com.yuriisurzhykov.pointdetector.di

import android.content.Context
import androidx.room.Room
import com.yuriisurzhykov.pointdetector.data.cache.CacheDatabase
import com.yuriisurzhykov.pointdetector.data.cache.configs.ConfigsDao
import com.yuriisurzhykov.pointdetector.data.cache.PointsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CacheDatabase {
        return Room.databaseBuilder(context, CacheDatabase::class.java, Constants.DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun providePlaceDao(database: CacheDatabase): PointsDao {
        return database.providePointsDao()
    }

    @Provides
    @Singleton
    fun provideConfigsDao(database: CacheDatabase): ConfigsDao {
        return database.provideConfigsDao()
    }

    object Constants {
        internal const val DATABASE_NAME = "cache_database.db"
    }

}