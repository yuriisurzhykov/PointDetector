package com.yuriisurzhykov.foodbanks.di.data

import com.yuriisurzhykov.foodbanks.core.data.DataStringConverter
import com.yuriisurzhykov.foodbanks.data.prefs.PreferencesDao
import com.yuriisurzhykov.foodbanks.data.prefs.SelectedCityPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {

    @Provides
    @Singleton
    fun provideSelectedCityPreference(
        converter: DataStringConverter,
        dao: PreferencesDao
    ): SelectedCityPreference {
        return SelectedCityPreference.Base(converter, dao)
    }
}