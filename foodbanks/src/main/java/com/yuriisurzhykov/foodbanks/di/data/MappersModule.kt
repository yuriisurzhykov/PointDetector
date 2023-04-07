package com.yuriisurzhykov.foodbanks.di.data

import com.google.gson.Gson
import com.yuriisurzhykov.foodbanks.core.data.DataStringConverter
import com.yuriisurzhykov.foodbanks.data.city.cloud.SnapshotCityMapper
import com.yuriisurzhykov.foodbanks.data.favorites.PointToFavoriteMapper
import com.yuriisurzhykov.foodbanks.data.point.CloudToCachePointMapper
import com.yuriisurzhykov.foodbanks.data.point.cloud.SnapshotPointMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MappersModule {

    @Provides
    @Singleton
    fun provideCitySnapshotMapper(): SnapshotCityMapper {
        return SnapshotCityMapper.Base()
    }

    @Provides
    @Singleton
    fun provideSnapshotPointMapper(): SnapshotPointMapper {
        return SnapshotPointMapper.Base()
    }

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideDataStringConverter(gson: Gson): DataStringConverter {
        return DataStringConverter.Base(gson)
    }

    @Provides
    @Singleton
    fun provideCloudToCachePointMapper(): CloudToCachePointMapper {
        return CloudToCachePointMapper.Base()
    }

    @Provides
    @Singleton
    fun providePointToFavoriteMapper(): PointToFavoriteMapper {
        return PointToFavoriteMapper.Base()
    }
}