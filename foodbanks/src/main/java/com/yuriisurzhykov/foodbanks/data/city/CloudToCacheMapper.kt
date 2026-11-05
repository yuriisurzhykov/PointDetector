package com.yuriisurzhykov.foodbanks.data.city

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.city.cloud.CityCloud
import com.yuriisurzhykov.geo.LatLng
import javax.inject.Inject

interface CloudToCacheMapper : Mapper<CityCloud, CityCache> {

    abstract class Abstract : CloudToCacheMapper, Mapper.Abstract<CityCloud, CityCache>() {

        override fun map(input: CityCloud): CityCache {
            val center = input.coordinates ?: pointsCenter(input)
            return CityCache(
                cityId = 0,
                name = input.name,
                nameCode = input.nameCode,
                region = input.region,
                country = input.country,
                latitude = center?.lat,
                longitude = center?.lng
            )
        }

        /**
         * When the cloud doesn't say where the city is, its center is the average
         * of its points. A city without points has no center.
         */
        private fun pointsCenter(city: CityCloud): LatLng? {
            val coordinates = city.points.map { it.coordinates }
            if (coordinates.isEmpty()) return null
            return LatLng(coordinates.map { it.lat }.average(), coordinates.map { it.lng }.average())
        }
    }

    class Base @Inject constructor() : Abstract()
}
