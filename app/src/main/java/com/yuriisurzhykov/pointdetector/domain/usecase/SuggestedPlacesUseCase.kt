package com.yuriisurzhykov.pointdetector.domain.usecase

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.core.TAG
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.Exception
import javax.inject.Inject

interface SuggestedPlacesUseCase {

    suspend fun getSuggestedPlaces(placeName: String): List<Point>

    open class Base @Inject constructor(
        @ApplicationContext context: Context,
        private val maxRelatedPlaceCount: Int,
        private val mapper: Mapper<Address, Point>
    ) : SuggestedPlacesUseCase {

        private val geocoder = Geocoder(context)

        @Suppress("BlockingMethodInNonBlockingContext")
        override suspend fun getSuggestedPlaces(placeName: String): List<Point> {
            return try {
                geocoder.getFromLocationName(placeName, maxRelatedPlaceCount).orEmpty().map { mapper.map(it) }
            } catch (e: Exception) {
                Log.e(TAG, "getSuggestedPlaces: error while decoding places", e)
                emptyList()
            }
        }
    }

}