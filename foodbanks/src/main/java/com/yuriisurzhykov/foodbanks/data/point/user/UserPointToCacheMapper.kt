package com.yuriisurzhykov.foodbanks.data.point.user

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.data.point.cache.HelpFactCache
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache
import com.yuriisurzhykov.foodbanks.data.point.cache.PointOrigin
import com.yuriisurzhykov.foodbanks.data.point.cache.PointWithHours
import com.yuriisurzhykov.foodbanks.data.point.cache.WorkingHourCache
import javax.inject.Inject

interface UserPointToCacheMapper : Mapper<UserPoint, PointWithHours> {

    abstract class Abstract : UserPointToCacheMapper,
        Mapper.Abstract<UserPoint, PointWithHours>() {

        override fun map(input: UserPoint) = PointWithHours(
            point = PointCache(
                pointId = 0,
                cityCodeId = input.cityCode,
                address = input.address,
                placeName = input.placeName,
                coordinates = input.coordinates,
                origin = PointOrigin.USER
            ),
            workingHours = input.workingHours.map {
                WorkingHourCache(it.dayName, it.dayValue, it.hoursFrom, it.hoursTo, 0)
            },
            helpFacts = input.help.map { (type, enabled) -> HelpFactCache(type, enabled, 0) }
        )
    }

    class Base @Inject constructor() : Abstract()
}
