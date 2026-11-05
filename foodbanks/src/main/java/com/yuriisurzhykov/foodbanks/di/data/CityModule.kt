package com.yuriisurzhykov.foodbanks.di.data

import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck
import com.yuriisurzhykov.foodbanks.data.city.CityRepository
import com.yuriisurzhykov.foodbanks.data.city.CloudToCacheListMapper
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCacheDataSource
import com.yuriisurzhykov.foodbanks.data.city.cache.CityDao
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloudDataSource
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityKeysCloud
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityListReader
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityMetaCloud
import com.yuriisurzhykov.foodbanks.data.city.cloud.SnapshotCityMapper
import com.yuriisurzhykov.foodbanks.data.city.cloud.ValueCityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CityModule {

    @Provides
    @Singleton
    fun provideCityCacheDataSource(dao: CityDao): CityCacheDataSource {
        return CityCacheDataSource.Base(dao)
    }

    @Provides
    @Singleton
    fun provideCityKeysCloud(database: FirebaseDatabase): CityKeysCloud = CityKeysCloud.Base(database)

    @Provides
    @Singleton
    fun provideCityMetaCloud(database: FirebaseDatabase): CityMetaCloud = CityMetaCloud.Base(database)

    @Provides
    @Singleton
    fun provideCityCloudDataSource(
        database: FirebaseDatabase,
        mapper: SnapshotCityMapper,
        keys: CityKeysCloud,
        meta: CityMetaCloud,
        valueMapper: ValueCityMapper
    ): CityCloudDataSource {
        return CityCloudDataSource.Base(database, mapper, CityListReader(keys, meta, valueMapper))
    }

    @Provides
    @Singleton
    fun provideCityRepository(
        cloudDataSource: CityCloudDataSource,
        cacheDataSource: CityDao,
        mapper: CloudToCacheListMapper,
        check: ConnectivityCheck,
    ): CityRepository {
        return CityRepository.Base(cloudDataSource, cacheDataSource, mapper, check)
    }
}