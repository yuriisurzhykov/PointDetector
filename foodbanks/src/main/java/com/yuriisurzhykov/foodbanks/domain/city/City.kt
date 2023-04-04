package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.core.presentation.Dispatchers
import com.yuriisurzhykov.foodbanks.data.prefs.PreferenceDataSource
import kotlinx.coroutines.CoroutineScope

interface City {

    interface Mapper<T> {

        fun map(cityName: String, cityCode: String, country: String, region: String): T

        class SaveToPreferences(
            private val preference: PreferenceDataSource<String>,
            private val dispatchers: Dispatchers,
            private val coroutineScope: CoroutineScope
        ) : Mapper<Unit> {

            override fun map(cityName: String, cityCode: String, country: String, region: String) {
                dispatchers.launchBackground(coroutineScope) {
                    preference.savePref(cityCode)
                }
            }
        }
    }

    fun <T> map(mapper: Mapper<T>): T

    class Base(
        private val cityName: String,
        private val cityCode: String,
        private val country: String,
        private val region: String
    ) : City {

        override fun <T> map(mapper: Mapper<T>): T = mapper.map(cityName, cityCode, country, region)
    }
}