package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import javax.inject.Inject

interface CityCacheToDomainMapper : Mapper<List<CityCache>, List<City>> {

    class Base @Inject constructor() : CityCacheToDomainMapper {

        private fun map(input: CityCache) = City(
            cityName = input.name,
            cityCode = input.nameCode,
            country = input.country,
            region = input.region
        )

        override fun map(input: List<CityCache>) = input.map { map(it) }
    }
}