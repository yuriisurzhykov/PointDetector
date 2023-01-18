package com.yuriisurzhykov.pointdetector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yuriisurzhykov.pointdetector.data.cache.configs.ConfigsDao
import com.yuriisurzhykov.pointdetector.data.cache.entities.ConfigEntity
import com.yuriisurzhykov.pointdetector.data.repository.ConfigsRepository
import com.yuriisurzhykov.pointdetector.fakes.FakeConfigsDao
import org.junit.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ConfigRepositoryTestCase {

    @Test
    fun `test saving and reading string config from repository`() = runBlocking {
        val fakeConfigDao: ConfigsDao = FakeConfigsDao()
        val repository = ConfigsRepository(fakeConfigDao)
        val testConfigName = "test_config_name"
        val expected = ConfigEntity(testConfigName, "Some value", String::class.java.name)
        repository.save(expected)
        val actual = repository.fetchByCondition(testConfigName)
        assertEquals(expected, actual)
        assertEquals(expected.configValue, actual?.configValue)
    }

}