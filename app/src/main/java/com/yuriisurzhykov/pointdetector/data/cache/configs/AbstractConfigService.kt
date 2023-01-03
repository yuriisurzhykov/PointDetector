package com.yuriisurzhykov.pointdetector.data.cache.configs

import com.google.gson.Gson
import com.yuriisurzhykov.pointdetector.data.cache.entities.ConfigEntity
import com.yuriisurzhykov.pointdetector.data.repository.ConfigsRepository

abstract class AbstractConfigService<T : Any>(
    private val dataProvider: ConfigsRepository,
    private val gson: Gson
) {

    abstract fun getConfigName(): String

    private fun getFullConfigName() = "config_${getConfigName()}"

    suspend fun createConfig(configValue: T) {
        val entity = ConfigEntity(
            getFullConfigName(), gson.toJson(configValue), configValue::class.java.name.orEmpty()
        )
        dataProvider.save(entity)
    }

    open suspend fun getConfigValue(): T? {
        val config: ConfigEntity? = dataProvider.fetchByCondition(getFullConfigName())
        return if (config != null) {
            gson.fromJson<T>(config.configValue, Class.forName(config.configDataType))
        } else null
    }
}