package com.yuriisurzhykov.pointdetector.domain.mappers

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.data.cache.LatLng
import javax.inject.Inject

class LocalToGoogleLatLngMapper @Inject constructor() :
    Mapper<LatLng, com.google.android.gms.maps.model.LatLng> {
    override fun map(from: LatLng): com.google.android.gms.maps.model.LatLng {
        return com.google.android.gms.maps.model.LatLng(from.lat, from.lng)
    }
}