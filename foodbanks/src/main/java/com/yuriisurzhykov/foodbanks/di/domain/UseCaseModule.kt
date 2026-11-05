package com.yuriisurzhykov.foodbanks.di.domain

import com.yuriisurzhykov.foodbanks.core.presentation.Dispatchers
import com.yuriisurzhykov.foodbanks.data.city.CityRepository
import com.yuriisurzhykov.foodbanks.data.favorites.FavoritesRepository
import com.yuriisurzhykov.foodbanks.data.point.PointsRepository
import com.yuriisurzhykov.foodbanks.data.point.user.UserPointsRepository
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import com.yuriisurzhykov.foodbanks.domain.city.CityCacheToDomainMapper
import com.yuriisurzhykov.foodbanks.domain.city.CityListUseCase
import com.yuriisurzhykov.foodbanks.domain.city.CityResponseMapper
import com.yuriisurzhykov.foodbanks.domain.favorites.FavoriteCacheMapper
import com.yuriisurzhykov.foodbanks.domain.favorites.FavoritesUseCase
import com.yuriisurzhykov.foodbanks.domain.points.PointCacheToDomainMapper
import com.yuriisurzhykov.foodbanks.domain.points.PointsListUseCase
import com.yuriisurzhykov.foodbanks.domain.points.PointsUseCaseResponseMapper
import com.yuriisurzhykov.foodbanks.domain.points.UserPointsUseCase
import com.yuriisurzhykov.foodbanks.domain.points.WorkingHourCacheToDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideDispatchers(): Dispatchers {
        return Dispatchers.Base()
    }

    @Provides
    @Singleton
    fun provideCityListUseCase(
        repository: CityRepository,
        preference: SelectedCityPreference,
        mapper: CityCacheToDomainMapper,
        dispatchers: Dispatchers
    ): CityListUseCase {
        return CityListUseCase.Base(repository, preference, CityResponseMapper(mapper), dispatchers)
    }

    @Provides
    @Singleton
    fun providePointsListUseCase(
        repository: PointsRepository,
        favoritesRepository: FavoritesRepository,
        mapper: PointsUseCaseResponseMapper,
        dispatchers: Dispatchers
    ): PointsListUseCase {
        return PointsListUseCase.Base(repository, favoritesRepository, mapper, dispatchers)
    }

    @Provides
    @Singleton
    fun providePointsUseCaseResponseMapper(): PointsUseCaseResponseMapper {
        return PointsUseCaseResponseMapper.Base(
            PointCacheToDomainMapper.Base(WorkingHourCacheToDomainMapper.Base())
        )
    }

    @Provides
    @Singleton
    fun provideUserPointsUseCase(
        preference: SelectedCityPreference,
        repository: UserPointsRepository
    ): UserPointsUseCase {
        return UserPointsUseCase.Base(preference, repository)
    }

    @Provides
    @Singleton
    fun provideFavoritesUseCase(
        dispatchers: Dispatchers,
        repository: FavoritesRepository
    ): FavoritesUseCase {
        return FavoritesUseCase.Base(FavoriteCacheMapper.Base(), dispatchers, repository)
    }
}
