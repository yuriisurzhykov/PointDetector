package com.yuriisurzhykov.foodbanks.domain.city

import com.yuriisurzhykov.foodbanks.core.domain.Select
import com.yuriisurzhykov.foodbanks.data.prefs.PreferenceDataSource

class City(
    private val cityName: String,
    private val cityCode: String,
    private val country: String,
    private val region: String
) : Select<String> {

    override suspend fun select(preference: PreferenceDataSource<String>) {
        preference.savePref(cityCode)
    }
}