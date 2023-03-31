package com.yuriisurzhykov.foodbanks.data.prefs

import com.yuriisurzhykov.foodbanks.core.data.DataStringConverter

interface PreferenceDataSource<T : Any> {

    @kotlin.jvm.Throws(NoPreferenceException::class)
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
            try {
                val pref = preferencesDao.preference(prefName())
                return stringConverter.inverseMap(pref.prefValue, clazz) as T
            } catch (e: Exception) {
                throw NoPreferenceException(prefName())
            }
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