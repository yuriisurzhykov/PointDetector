package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.core.data.map
import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponse
import com.yuriisurzhykov.foodbanks.data.city.CityRepository
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface CityListUseCase {

    suspend fun cities(): Flow<UseCaseResponse<List<City>>>

    suspend fun chooseCity(city: City)

    abstract class Abstract(
        private val cityRepository: CityRepository,
        private val preference: SelectedCityPreference,
        private val mapper: CityResponseMapper
    ) : CityListUseCase {

        override suspend fun cities(): Flow<UseCaseResponse<List<City>>> {
            return cityRepository.fetch().map { it.map(mapper) }
        }

        override suspend fun chooseCity(city: City) {
            city.select(preference)
        }
    }

    class Base @Inject constructor(
        cityRepository: CityRepository,
        preference: SelectedCityPreference,
        mapper: CityResponseMapper
    ) : Abstract(cityRepository, preference, mapper)
}