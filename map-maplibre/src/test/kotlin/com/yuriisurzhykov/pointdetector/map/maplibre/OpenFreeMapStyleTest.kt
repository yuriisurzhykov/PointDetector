package com.yuriisurzhykov.pointdetector.map.maplibre

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OpenFreeMapStyleTest {

    private val colors = MapStyleColors(
        land = 0xFFEAF0F8.toInt(),
        water = 0xFFCFE2F6.toInt(),
        park = 0xFFDCEBDD.toInt(),
        road = 0xFFFFFFFF.toInt(),
        edge = 0xFFD6DEEA.toInt(),
        label = 0xFF4A5A73.toInt(),
        labelHalo = 0xFFEAF0F8.toInt(),
    )

    private val style: JsonObject = Json.parseToJsonElement(OpenFreeMapStyle.json(colors)).jsonObject

    @Test
    fun `test the style is valid json of version 8 with the free tile source`() {
        assertEquals("8", style.getValue("version").jsonPrimitive.content)
        val source = style.getValue("sources").jsonObject.getValue("openmaptiles").jsonObject
        assertEquals("vector", source.getValue("type").jsonPrimitive.content)
        assertTrue(source.getValue("url").jsonPrimitive.content.startsWith("https://tiles.openfreemap.org/"))
        assertTrue(style.getValue("glyphs").jsonPrimitive.content.contains("{fontstack}"))
    }

    @Test
    fun `test every layer has a unique id and uses the declared source`() {
        val layers: JsonArray = style.getValue("layers").jsonArray
        val ids = layers.map { it.jsonObject.getValue("id").jsonPrimitive.content }

        assertEquals(ids.size, ids.toSet().size)
        layers.map { it.jsonObject }.filter { it["type"]?.jsonPrimitive?.content != "background" }.forEach {
            assertEquals("openmaptiles", it.getValue("source").jsonPrimitive.content)
        }
    }

    @Test
    fun `test the theme colours are written as hex`() {
        val text = OpenFreeMapStyle.json(colors)

        assertTrue(text.contains("#EAF0F8"))
        assertTrue(text.contains("#CFE2F6"))
        assertTrue(text.contains("#DCEBDD"))
        assertTrue(text.contains("#D6DEEA"))
    }

    @Test
    fun `test the attribution names the data source`() {
        assertTrue(OpenFreeMapStyle.ATTRIBUTION.contains("OpenStreetMap"))
    }
}
