package com.yuriisurzhykov.foodbanks.data.city.cloud

import com.yuriisurzhykov.foodbanks.core.data.Mapper
import com.yuriisurzhykov.foodbanks.core.data.MappingException
import com.yuriisurzhykov.foodbanks.core.data.SnapshotValue
import com.yuriisurzhykov.foodbanks.core.data.map
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud
import com.yuriisurzhykov.foodbanks.data.point.cloud.ValuePointMapper
import com.yuriisurzhykov.geo.LatLng
import javax.inject.Inject

interface ValueCityMapper : Mapper<SnapshotValue, CityCloud> {

    abstract class Abstract(
        private val pointMapper: ValuePointMapper
    ) : ValueCityMapper, Mapper.Abstract<SnapshotValue, CityCloud>() {

        override fun map(input: SnapshotValue): CityCloud {
            val nameCode = input.string("name_code")
            return CityCloud(
                name = input.string("name"),
                region = input.stringOrEmpty("region"),
                nameCode = nameCode,
                country = input.stringOrEmpty("country"),
                points = input.values("points").mapNotNull { point -> readable(point, nameCode) },
                coordinates = input.valueOrNull("coordinates")?.let {
                    LatLng(it.double("lat"), it.double("lng"))
                }
            )
        }

        /**
         * A point that cannot be read (no coordinates, no address) is left out, because a point
         * that cannot be placed cannot be shown. One bad point must not stop the sync of the city.
         */
        private fun readable(point: SnapshotValue, cityCode: String): PointCloud? = try {
            point.map(pointMapper).copy(cityCode = cityCode)
        } catch (e: MappingException) {
            null
        }
    }

    class Base @Inject constructor(pointMapper: ValuePointMapper) : Abstract(pointMapper)
}
