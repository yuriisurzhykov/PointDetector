package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.core.domain.UseCaseResponse
import kotlinx.coroutines.flow.Flow

interface CityListUseCase {

    suspend fun cities(): Flow<UseCaseResponse<City>>

    suspend fun chooseCity(city: City)
}