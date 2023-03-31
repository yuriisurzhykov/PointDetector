package com.yuriisurzhykov.foodbanks.data.city

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloud
import javax.inject.Inject

interface CloudToCacheMapper : Mapper<CityCloud, CityCache> {

    abstract class Abstract : CloudToCacheMapper, Mapper.Abstract<CityCloud, CityCache>() {
        override fun map(input: CityCloud): CityCache {
            return CityCache(
                cityId = 0,
                name = input.name,
                nameCode = input.nameCode,
                region = input.region,
                country = input.country
            )
        }
    }

    class Base @Inject constructor() : Abstract()
}