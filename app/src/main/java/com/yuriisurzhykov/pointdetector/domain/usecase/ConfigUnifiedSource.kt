package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.data.cache.configs.MeasureUnitConfig
import com.yuriisurzhykov.pointdetector.data.cache.configs.SortingTypeConfig
import javax.inject.Inject

interface ConfigUnifiedSource {

    suspend fun measureConfigValue(): String
    suspend fun sortTypeConfigValue(): String

    class Base @Inject constructor(
        private val measureUnitConfig: MeasureUnitConfig,
        private val sortingTypeConfig: SortingTypeConfig
    ) : ConfigUnifiedSource {

        override suspend fun measureConfigValue() = measureUnitConfig.getConfigValue()
        override suspend fun sortTypeConfigValue() = sortingTypeConfig.getConfigValue()
    }
}