package com.yuriisurzhykov.pointdetector.domain.usecase

import android.content.Context
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.jvm.Throws

interface IGeoDecodeUseCase {

    suspend fun decodeToLocation(placeName: String): Address

    open class Base @Inject constructor(
        @ApplicationContext context: Context,
        private val maxRelatedPlacesCount: Int
    ) : IGeoDecodeUseCase {

        private val geoDecoder = Geocoder(context, Locale.getDefault())

        @Throws(Resources.NotFoundException::class)
        override suspend fun decodeToLocation(placeName: String): Address = withContext(Dispatchers.IO) {
            val decoded = geoDecoder.getFromLocationName(placeName, maxRelatedPlacesCount)
            if (decoded.isNotEmpty()) {
                return@withContext decoded.first()
            } else {
                throw Resources.NotFoundException("Address with name $placeName not found!")
            }
        }
    }

}