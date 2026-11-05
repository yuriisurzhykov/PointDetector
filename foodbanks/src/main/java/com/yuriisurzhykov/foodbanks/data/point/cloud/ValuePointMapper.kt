package com.yuriisurzhykov.foodbanks.data.point.cloud

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.core.data.SnapshotValue
import com.yuriisurzhykov.geo.LatLng
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import javax.inject.Inject

/**
 * The cloud stores points inside of their city, so a point doesn't know the city code
 * by itself. It is up to the city mapper to fill it in.
 */
interface ValuePointMapper : Mapper<SnapshotValue, PointCloud> {

    abstract class Abstract : ValuePointMapper, Mapper.Abstract<SnapshotValue, PointCloud>() {

        override fun map(input: SnapshotValue): PointCloud {
            val coordinates = input.value("coordinates")
            return PointCloud(
                address = input.string("address"),
                coordinates = LatLng(coordinates.double("lat"), coordinates.double("lng")),
                placeName = input.string("placeName"),
                cityCode = "",
                workingHours = input.values("workingHours").map { hour ->
                    WorkingHour(
                        dayName = hour.string("dayName"),
                        dayValue = hour.int("dayValue"),
                        hoursFrom = hour.string("hoursFrom"),
                        hoursTo = hour.string("hoursTo")
                    )
                },
                help = input.booleans("help")
            )
        }
    }

    class Base @Inject constructor() : Abstract()
}
