package com.yuriisurzhykov.pointdetector.domain.mappers

import android.location.Address
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.entities.LatLng
import com.yuriisurzhykov.pointdetector.data.remote.DistanceCalculateService
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.IUserLocationService
import javax.inject.Inject

class AddressToPointMapper @Inject constructor(
    private val distanceCalculateService: DistanceCalculateService,
    private val userLocationService: IUserLocationService
) : Mapper<Address, Point> {
    override fun map(from: Address): Point {
        val placeLatLng = LatLng(from.latitude, from.longitude)
        return Point(
            from.getAddressLine(0),
            placeLatLng,
            distanceCalculateService.calculateDistance(
                userLocationService.currentUserLocation(),
                placeLatLng
            ),
            from.featureName.orEmpty(),
            emptyList(),
            false
        )
    }
}