package com.yuriisurzhykov.foodbanks.core.domain

import com.yuriisurzhykov.foodbanks.data.prefs.PreferenceDataSource

interface Select<T : Any> {
    suspend fun select(preference: PreferenceDataSource<T>)
}