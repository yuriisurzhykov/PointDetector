package com.yuriisurzhykov.pointdetector.data.repository

import com.yuriisurzhykov.pointdetector.core.*
import com.yuriisurzhykov.pointdetector.data.cache.configs.ConfigsDao
import com.yuriisurzhykov.pointdetector.data.cache.entities.ConfigEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConfigsRepository @Inject constructor(
    private val configsDao: ConfigsDao
) : Save<ConfigEntity>,
    Fetch<Flow<List<ConfigEntity>>>,
    ConditionFetch<String, ConfigEntity?>,
    Delete<ConfigEntity> {

    override suspend fun delete(entity: ConfigEntity) {
        configsDao.delete(entity)
    }

    override suspend fun fetch(): Flow<List<ConfigEntity>> {
        return configsDao.fetchAllConfigs().asFlow()
    }

    override suspend fun fetchByCondition(condition: String): ConfigEntity? {
        return configsDao.findConfig(condition)
    }

    override suspend fun save(entity: ConfigEntity) {
        configsDao.insert(entity)
    }

    override suspend fun save(list: List<ConfigEntity>) {
        list.forEach { configsDao.insert(it) }
    }
}