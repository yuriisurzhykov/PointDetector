package com.yuriisurzhykov.pointdetector.fakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yuriisurzhykov.pointdetector.data.cache.configs.ConfigsDao
import com.yuriisurzhykov.pointdetector.data.cache.entities.ConfigEntity

class FakeConfigsDao : ConfigsDao {

    private val entities = mutableListOf<ConfigEntity>()

    override suspend fun insert(entity: ConfigEntity) {
        entities.add(entity)
    }

    override suspend fun update(entity: ConfigEntity) {
        val found = entities.find { it.configName == entity.configName }
        if (found != null) {
            val position = entities.indexOf(found)
            entities.removeAt(position)
            entities.add(position, entity)
        }
    }

    override suspend fun delete(entity: ConfigEntity) {
        entities.removeIf { it.configName == entity.configName }
    }

    override suspend fun findConfig(configName: String): ConfigEntity? {
        return entities.find { it.configName == configName }
    }

    override fun fetchAllConfigs(): LiveData<List<ConfigEntity>> {
        return MutableLiveData(entities)
    }
}