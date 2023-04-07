package com.yuriisurzhykov.foodbanks.di.data

import com.yuriisurzhykov.foodbanks.data.FoodbankDatabase
import com.yuriisurzhykov.foodbanks.data.city.cache.CityDao
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesDao
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsDao
import com.yuriisurzhykov.foodbanks.data.prefs.PreferencesDao
import com.yuriisurzhykov.foodbanks.data.sync.cache.SyncDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun provideCityDao(database: FoodbankDatabase): CityDao {
        return database.cityDao()
    }

    @Provides
    @Singleton
    fun providePreferencesDao(database: FoodbankDatabase): PreferencesDao {
        return database.preferenceDao()
    }

    @Provides
    @Singleton
    fun providePointsDao(database: FoodbankDatabase): PointsDao {
        return database.pointsDao()
    }

    @Provides
    @Singleton
    fun provideSyncDao(database: FoodbankDatabase): SyncDao {
        return database.syncDao()
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(database: FoodbankDatabase): FavoritesDao {
        return database.favoritesDao()
    }
}