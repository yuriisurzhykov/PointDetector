package com.yuriisurzhykov.geo.di.domain

import com.yuriisurzhykov.geo.domain.DistanceCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DistanceModule {

    @Provides
    @Singleton
    fun provideDistanceCalculator(): DistanceCalculator {
        return DistanceCalculator.Base()
    }
}
