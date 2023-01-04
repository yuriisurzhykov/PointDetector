package com.yuriisurzhykov.pointdetector.domain.mappers

import android.location.Address
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import javax.inject.Inject

class AddressToPointMapper @Inject constructor() : Mapper<Address, Point> {
    override fun map(from: Address): Point {
        val placeLatLng = LatLng(from.latitude, from.longitude)
        return Point(
            from.getAddressLine(0), placeLatLng, from.featureName.orEmpty(), emptyList()
        )
    }
}