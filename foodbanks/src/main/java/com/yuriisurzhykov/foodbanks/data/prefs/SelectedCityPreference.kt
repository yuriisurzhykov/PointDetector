package com.yuriisurzhykov.foodbanks.data.prefs

import com.yuriisurzhykov.foodbanks.core.DataStringConverter
import javax.inject.Inject

interface SelectedCityPreference : PreferenceDataSource<Long> {

    class Base @Inject constructor(
        stringConverter: DataStringConverter,
        preferencesDao: PreferencesDao
    ) : SelectedCityPreference,
        PreferenceDataSource.Abstract<Long>(stringConverter, preferencesDao, Long::class.java) {

        override fun prefName(): String = "preference_selected_city"
    }
}