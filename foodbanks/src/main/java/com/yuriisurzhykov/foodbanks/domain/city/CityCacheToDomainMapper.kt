package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import javax.inject.Inject

interface CityCacheToDomainMapper : Mapper.List<CityCache, City> {

    class Base @Inject constructor() :
        CityCacheToDomainMapper,
        Mapper.AbstractList<CityCache, City>() {

        override fun map(input: CityCache) = City.Base(
            cityName = input.name,
            cityCode = input.nameCode,
            country = input.country,
            region = input.region
        )
    }
}