package com.yuriisurzhykov.pointdetector.domain.services

import com.yuriisurzhykov.pointdetector.data.cache.LatLng

interface IUserLocationService {

    fun currentUserLocation(): LatLng

}