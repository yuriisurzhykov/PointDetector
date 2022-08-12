package com.yuriisurzhykov.pointdetector.domain.usecase

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ISuggestedPlacesUseCase {

    suspend fun getSuggestedPlaces(planeName: String): List<Point>

    open class Base @Inject constructor(
        @ApplicationContext context: Context,
        private val maxRelatedPlaceCount: Int,
        private val mapper: Mapper<Address, Point>
    ) : ISuggestedPlacesUseCase {

        private val geocoder = Geocoder(context)

        override suspend fun getSuggestedPlaces(placeName: String): List<Point> {
            return geocoder.getFromLocationName(placeName, maxRelatedPlaceCount).map { mapper.map(it) }
        }
    }

}