package com.yuriisurzhykov.foodbanks.data.prefs

import com.yuriisurzhykov.foodbanks.core.data.DataStringConverter
import javax.inject.Inject

interface SelectedCityPreference : PreferenceDataSource<String> {

    class Base @Inject constructor(
        stringConverter: DataStringConverter,
        preferencesDao: PreferencesDao
    ) : PreferenceDataSource.Abstract<String>(
        stringConverter, preferencesDao, String::class.java
    ), SelectedCityPreference {

        override fun prefName(): String = "preference_selected_city"
    }
}