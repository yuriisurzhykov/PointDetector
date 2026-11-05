package com.yuriisurzhykov.pointdetector.di

import android.content.Context
import android.location.Address
import com.yuriisurzhykov.foodbanks.domain.city.SelectedCityUseCase
import com.yuriisurzhykov.foodbanks.domain.favorites.FavoritesUseCase
import com.yuriisurzhykov.foodbanks.domain.points.PointsListUseCase
import com.yuriisurzhykov.foodbanks.domain.points.UserPointsUseCase
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.core.SuspendMapper
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.source.DateFormatterType
import com.yuriisurzhykov.pointdetector.domain.usecase.CheckTimeAvailabilityUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesApplyUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesFetchUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.FavoritesRemoveUseCase
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessages
import com.yuriisurzhykov.pointdetector.domain.usecase.FetchAllPointsUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.RemoveUserPointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.GeoDecodeUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SavePointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SearchPointUseCase
import com.yuriisurzhykov.pointdetector.domain.usecase.SuggestedPlacesUseCase
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.yuriisurzhykov.foodbanks.domain.points.Point as FoodbankPoint

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideFetchAllPointsUseCase(
        selectedCityUseCase: SelectedCityUseCase,
        pointsListUseCase: PointsListUseCase,
        mapper: SuspendMapper<List<FoodbankPoint>, List<PointUi>>,
        messages: AppMessages,
    ): FetchAllPointsUseCase {
        return FetchAllPointsUseCase.Base(selectedCityUseCase, pointsListUseCase, mapper, messages)
    }

    @Provides
    @Singleton
    fun provideRemoveUserPointUseCase(userPointsUseCase: UserPointsUseCase): RemoveUserPointUseCase {
        return RemoveUserPointUseCase.Base(userPointsUseCase)
    }

    @Provides
    @Singleton
    fun provideSuggestedPlacesUseCase(
        @ApplicationContext context: Context,
        mapper: Mapper<Address, Point>,
    ): SuggestedPlacesUseCase {
        return SuggestedPlacesUseCase.Base(context, 5, mapper)
    }

    @Provides
    @Singleton
    fun provideGeoDecodeUseCase(
        @ApplicationContext context: Context,
    ): GeoDecodeUseCase {
        return GeoDecodeUseCase.Base(context, 5)
    }

    @Provides
    @Singleton
    fun provideSavePointUseCase(
        selectedCityUseCase: SelectedCityUseCase,
        userPointsUseCase: UserPointsUseCase,
    ): SavePointUseCase {
        return SavePointUseCase.Base(selectedCityUseCase, userPointsUseCase)
    }

    @Provides
    @Singleton
    fun provideSearchPointUseCase(
        fetchAllPointsUseCase: FetchAllPointsUseCase,
    ): SearchPointUseCase {
        return SearchPointUseCase.Base(fetchAllPointsUseCase)
    }

    @Provides
    @Singleton
    fun provideCheckTimeAvailabilityUseCase(
        formatter: DateFormatterType.TimeFormat,
    ): CheckTimeAvailabilityUseCase {
        return CheckTimeAvailabilityUseCase.Base(formatter)
    }

    @Provides
    @Singleton
    fun provideFavoritesFetchUseCase(
        fetchAllPointsUseCase: FetchAllPointsUseCase,
    ): FavoritesFetchUseCase {
        return FavoritesFetchUseCase.Base(fetchAllPointsUseCase)
    }

    @Provides
    @Singleton
    fun provideFavoritesRemoveUseCase(
        favoritesUseCase: FavoritesUseCase,
    ): FavoritesRemoveUseCase {
        return FavoritesRemoveUseCase.Base(favoritesUseCase)
    }

    @Provides
    @Singleton
    fun provideFavoritesApplyUseCase(
        favoritesUseCase: FavoritesUseCase,
    ): FavoritesApplyUseCase {
        return FavoritesApplyUseCase.Base(favoritesUseCase)
    }

}
