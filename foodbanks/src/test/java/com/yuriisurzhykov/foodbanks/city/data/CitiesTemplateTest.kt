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
 * The template in firebase/cities.template.json is what gets imported into the cloud,
 * so it must stay readable by the app.
 */
class CitiesTemplateTest {

    private val mapper = ValueCityMapper.Base(ValuePointMapper.Base())

    @Test
    fun `test every city of the template is mapped`() {
        val cities = File("firebase/cities.template.json").reader().use {
            Gson().fromJson(it, Map::class.java)
        }

        assertTrue(cities.isNotEmpty())
        cities.forEach { (key, value) ->
            val city = mapper.map(SnapshotValue.Base(value as Map<*, *>))

            assertEquals("city key must be equal to name_code", key, city.nameCode)
            assertTrue(city.points.isNotEmpty())
            assertTrue(city.points.all { it.cityCode == city.nameCode })
            assertTrue(city.points.all { it.workingHours.isNotEmpty() })
        }
    }
}
