package com.yuriisurzhykov.pointdetector.di

import android.location.Address
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.mappers.AddressToPointMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.LocalToGoogleLatLngMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.PointToUiMapper
import com.yuriisurzhykov.pointdetector.domain.mappers.PointsToUiListMapper
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.yuriisurzhykov.foodbanks.domain.points.Point as FoodbankPoint

@Module
@InstallIn(SingletonComponent::class)
abstract class MappersModule {

    @Binds
    @Singleton
    abstract fun bindPointToUiMapper(mapper: PointToUiMapper): SuspendMapper<FoodbankPoint, PointUi>

    @Binds
    @Singleton
    abstract fun bindPointsToUiListMapper(
        mapper: PointsToUiListMapper
    ): SuspendMapper<List<FoodbankPoint>, List<PointUi>>

    companion object {

        @Provides
        @Singleton
        fun provideLocaleToGoogleLatLngMapper(): Mapper<LatLng, com.google.android.gms.maps.model.LatLng> {
            return LocalToGoogleLatLngMapper()
        }

        @Provides
        @Singleton
        fun provideAddressToPointMapper(): Mapper<Address, Point> {
            return AddressToPointMapper()
        }
    }
}
