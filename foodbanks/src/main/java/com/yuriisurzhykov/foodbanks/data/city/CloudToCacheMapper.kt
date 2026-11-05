package com.yuriisurzhykov.foodbanks.data.city

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloud
import javax.inject.Inject

interface CloudToCacheMapper : Mapper<CityCloud, CityCache> {

    abstract class Abstract : CloudToCacheMapper, Mapper.Abstract<CityCloud, CityCache>() {

        /**
         * The cloud doesn't store city coordinates, so the city center is the average
         * of its points. A city without points has no center.
         */
        override fun map(input: CityCloud): CityCache {
            val coordinates = input.points.map { it.coordinates }
            return CityCache(
                cityId = 0,
                name = input.name,
                nameCode = input.nameCode,
                region = input.region,
                country = input.country,
                latitude = coordinates.map { it.lat }.takeIf { it.isNotEmpty() }?.average(),
                longitude = coordinates.map { it.lng }.takeIf { it.isNotEmpty() }?.average()
            )
        }
    }

    class Base @Inject constructor() : Abstract()
}
