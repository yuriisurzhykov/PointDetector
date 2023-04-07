package com.yuriisurzhykov.foodbanks.di.data

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck
import com.yuriisurzhykov.foodbanks.data.point.CloudToCachePointMapper
import com.yuriisurzhykov.foodbanks.data.point.PointsRepository
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsCacheDataSource
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsDao
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloudDataSource
import com.yuriisurzhykov.foodbanks.data.point.cloud.SnapshotPointMapper
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PointsModule {

    @Provides
    @Singleton
    fun providePointsCacheDataSource(
        preference: SelectedCityPreference,
        pointsDao: PointsDao
    ): PointsCacheDataSource {
        return PointsCacheDataSource.Base(preference, pointsDao)
    }

    @Provides
    @Singleton
    fun providePointsCloudDataSource(
        database: FirebaseDatabase,
        mapper: SnapshotPointMapper
    ): PointCloudDataSource {
        return PointCloudDataSource.Base(database, mapper)
    }

    @Provides
    @Singleton
    fun providePointsRepository(
        dao: PointsDao,
        preference: SelectedCityPreference,
        source: PointCloudDataSource,
        mapper: CloudToCachePointMapper,
        check: ConnectivityCheck
    ): PointsRepository {
        return PointsRepository.Base(dao, preference, source, mapper, check)
    }
}