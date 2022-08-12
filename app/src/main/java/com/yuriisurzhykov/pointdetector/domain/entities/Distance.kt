package com.yuriisurzhykov.pointdetector.domain.entities

import android.content.Context
import com.yuriisurzhykov.pointdetector.R

sealed class Distance(protected val meters: Double) {

    abstract fun getDistanceLocale(context: Context): String

    class Kilometers(meters: Double) : Distance(meters) {
        override fun getDistanceLocale(context: Context): String {
            return String.format(context.getString(R.string.kilometers_format), meters / 1000)
        }
    }

    class Miles(meters: Double) : Distance(meters) {
        override fun getDistanceLocale(context: Context): String {
            return String.format(context.getString(R.string.miles_format), meters / 1609)
        }
    }

}