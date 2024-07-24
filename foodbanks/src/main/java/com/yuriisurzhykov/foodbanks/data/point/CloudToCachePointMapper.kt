package com.yuriisurzhykov.foodbanks.data.point

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache
import com.yuriisurzhykov.foodbanks.data.point.cache.PointWithHours
import com.yuriisurzhykov.foodbanks.data.point.cache.WorkingHourCache
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud
import javax.inject.Inject

interface CloudToCachePointMapper : Mapper<List<PointCloud>, List<PointWithHours>> {

    fun map(input: PointCloud): PointWithHours

    class Base @Inject constructor() : CloudToCachePointMapper {

        override fun map(input: List<PointCloud>): List<PointWithHours> {
            return input.map { map(it) }
        }

        override fun map(input: PointCloud): PointWithHours {
            return PointWithHours(
                point = PointCache(
                    pointId = 0,
                    cityCodeId = input.cityCode,
                    address = input.address,
                    placeName = input.placeName,
                    coordinates = input.coordinates
                ),
                workingHours = input.workingHours.map {
                    WorkingHourCache(
                        it.dayName,
                        it.dayValue,
                        it.hoursFrom,
                        it.hoursTo,
                        0
                    )
                }
            )
        }
    }
}