package com.yuriisurzhykov.pointdetector

import com.google.gson.Gson
import com.yuriisurzhykov.pointdetector.data.cache.configs.AbstractConfigService
import com.yuriisurzhykov.pointdetector.data.cache.entities.ConfigEntity
import com.yuriisurzhykov.pointdetector.data.repository.ConfigsRepository
import com.yuriisurzhykov.pointdetector.fakes.FakeConfigsDao
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates.notNull

class AbstractConfigServiceTest {

    private var gson: Gson by notNull()
    private var configRepository: ConfigsRepository by notNull()

    @Before
    fun `init dependencies`() {
        gson = Gson()
        configRepository = ConfigsRepository(FakeConfigsDao())
    }

    @Test
    fun `test abstract config service for string data type`() = runBlocking {
        val configName = "test_config_name"
        val configService = object : AbstractConfigService<String>(configRepository, gson) {
            override fun getConfigName() = configName
        }
        val expected = "Some value for store"
        configService.createConfig(expected)
        val actual = configService.getConfigValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `test abstract config service for user data type`() = runBlocking {
        val configName = "test_config_name_2"
        val configService = object : AbstractConfigService<TestClass>(configRepository, gson) {
            override fun getConfigName() = configName
        }
        val expected = TestClass("some value", 1)
        configService.createConfig(expected)
        val actual = configService.getConfigValue()
        assertEquals(expected, actual)
    }

    data class TestClass(var dataValue: String, var secondDataValue: Int)
}