package com.yuriisurzhykov.foodbanks.data.prefs

import com.yuriisurzhykov.foodbanks.core.data.DataStringConverter
import javax.inject.Inject

/**
 * How the city was chosen: [AUTOMATIC] when the app took the city where the person is, [MANUAL]
 * when the person picked it from the list. Not saved yet means "not known", which is not automatic.
 */
interface CityModePreference : PreferenceDataSource<String> {

    class Base @Inject constructor(
        stringConverter: DataStringConverter,
        preferencesDao: PreferencesDao
    ) : PreferenceDataSource.Abstract<String>(
        stringConverter, preferencesDao, String::class.java
    ), CityModePreference {

        override fun prefName(): String = "preference_city_mode"
    }

    companion object {
        const val AUTOMATIC = "automatic"
        const val MANUAL = "manual"
    }
}
