package com.yuriisurzhykov.foodbanks.data.prefs

import com.yuriisurzhykov.foodbanks.core.DataStringConverter

interface PreferenceDataSource<T : Any> {

    suspend fun getPrefValue(): T

    suspend fun savePref(value: T)

    suspend fun updatePref(value: T)

    abstract class Abstract<T : Any>(
        private val stringConverter: DataStringConverter,
        private val preferencesDao: PreferencesDao,
        private val clazz: Class<T>
    ) : PreferenceDataSource<T> {

        protected abstract fun prefName(): String

        @Suppress("UNCHECKED_CAST")
        override suspend fun getPrefValue(): T {
            val pref = preferencesDao.preference(prefName())
            return stringConverter.inverseMap(pref.prefValue, clazz) as T
        }

        override suspend fun savePref(value: T) {
            preferencesDao.insert(
                PreferenceCache(0, prefName(), stringConverter.map(value))
            )
        }

        override suspend fun updatePref(value: T) {
            val preference = preferencesDao.preference(prefName())
            preferencesDao.update(preference.copy(prefValue = stringConverter.map(value)))
        }
    }
}