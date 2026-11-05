package com.yuriisurzhykov.pointdetector.di

import com.yuriisurzhykov.pointdetector.map.maplibre.MapLibreMapFactory
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.map.PdMapFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The only place that knows which map the app uses. To change the map, add another module that
 * implements [PdMapFactory] and return it here; no screen changes.
 */
@Module
@InstallIn(SingletonComponent::class)
object MapModule {

    @Provides
    @Singleton
    fun provideMapFactory(): PdMapFactory = MapLibreMapFactory()
}
