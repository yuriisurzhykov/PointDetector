package com.yuriisurzhykov.foodbanks.data.point

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud
import javax.inject.Inject

interface CloudToCachePointMapper : Mapper<List<PointCloud>, List<PointCache>> {

    fun map(input: PointCloud): PointCache

    class Base @Inject constructor() : CloudToCachePointMapper {

        override fun map(input: List<PointCloud>): List<PointCache> {
            return input.map { map(it) }
        }

        override fun map(input: PointCloud): PointCache {
            return PointCache(
                pointId = 0,
                cityCodeId = input.cityCode,
                address = input.address,
                placeName = input.placeName,
                coordinates = input.coordinates,
                workingHours = input.workingHours
            )
        }
    }
}