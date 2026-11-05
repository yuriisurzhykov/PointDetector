package com.yuriisurzhykov.foodbanks.city.data

import com.google.gson.Gson
import com.yuriisurzhykov.foodbanks.core.data.SnapshotValue
import com.yuriisurzhykov.foodbanks.data.city.cloud.ValueCityMapper
import com.yuriisurzhykov.foodbanks.data.point.cloud.ValuePointMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * The files in firebase/ are what gets imported into the cloud, so they must stay
 * readable by the app.
 */
class CitiesTemplateTest {

    private val mapper = ValueCityMapper.Base(ValuePointMapper.Base())

    @Test
    fun `test there are files to import`() {
        assertTrue(importFiles().any { it.name == "cities.template.json" })
        assertTrue(importFiles().any { it.name.startsWith("root.") })
    }

    @Test
    fun `test every city of every file is mapped`() {
        importFiles().forEach { file ->
            val cities = cities(file)

            assertTrue("${file.name} is empty", cities.isNotEmpty())
            cities.forEach { (key, value) ->
                val city = mapper.map(SnapshotValue.Base(value as Map<*, *>))

                assertEquals("${file.name}: city key must be equal to name_code", key, city.nameCode)
                assertTrue("${file.name}: ${city.name} has no points", city.points.isNotEmpty())
                assertTrue(city.points.all { it.cityCode == city.nameCode })
                assertTrue(
                    "${file.name}: a point has no working hours",
                    city.points.all { it.workingHours.isNotEmpty() }
                )
            }
        }
    }

    @Test
    fun `test addresses are unique inside of a city`() {
        importFiles().forEach { file ->
            cities(file).values.forEach { value ->
                val city = mapper.map(SnapshotValue.Base(value as Map<*, *>))
                val addresses = city.points.map { it.address }

                assertEquals(
                    "${file.name}: addresses of ${city.name} must be unique",
                    addresses.size, addresses.toSet().size
                )
            }
        }
    }

    /**
     * The files imported into the root of the database keep the cities inside of "cities".
     */
    private fun cities(file: File): Map<*, *> {
        val content = file.reader().use { Gson().fromJson(it, Map::class.java) }
        return if (file.name.startsWith("root.")) content["cities"] as Map<*, *> else content
    }

    private fun importFiles(): List<File> {
        return File("firebase").listFiles { file -> file.extension == "json" }.orEmpty().toList()
    }
}
