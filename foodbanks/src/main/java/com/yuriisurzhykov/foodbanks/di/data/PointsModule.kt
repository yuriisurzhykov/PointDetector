package com.yuriisurzhykov.foodbanks.di.data

import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloudDataSource
import com.yuriisurzhykov.foodbanks.data.point.CloudToCachePointMapper
import com.yuriisurzhykov.foodbanks.data.point.PointsRepository
import com.yuriisurzhykov.foodbanks.data.point.PointsSource
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsCacheDataSource
import com.yuriisurzhykov.foodbanks.data.point.cache.PointsDao
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloudDataSource
import com.yuriisurzhykov.foodbanks.data.point.user.UserPointToCacheMapper
import com.yuriisurzhykov.foodbanks.data.point.user.UserPointsRepository
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
        cityCloudDataSource: CityCloudDataSource
    ): PointCloudDataSource {
        return PointCloudDataSource.Base(cityCloudDataSource)
    }

    /**
     * The only place that knows which sources the points are made of. To add one more kind
     * of points, add its source here.
     */
    @Provides
    @Singleton
    fun providePointsSource(dao: PointsDao): PointsSource {
        return PointsSource.Combined(listOf(PointsSource.Cloud(dao), PointsSource.User(dao)))
    }

    @Provides
    @Singleton
    fun providePointsRepository(
        dao: PointsDao,
        pointsSource: PointsSource,
        preference: SelectedCityPreference,
        source: PointCloudDataSource,
        mapper: CloudToCachePointMapper,
        check: ConnectivityCheck
    ): PointsRepository {
        return PointsRepository.Base(dao, pointsSource, preference, source, mapper, check)
    }

    @Provides
    @Singleton
    fun provideUserPointToCacheMapper(): UserPointToCacheMapper {
        return UserPointToCacheMapper.Base()
    }

    @Provides
    @Singleton
    fun provideUserPointsRepository(
        dao: PointsDao,
        mapper: UserPointToCacheMapper
    ): UserPointsRepository {
        return UserPointsRepository.Base(dao, mapper)
    }
}
