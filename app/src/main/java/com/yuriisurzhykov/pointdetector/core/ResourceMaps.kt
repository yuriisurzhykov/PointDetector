package com.yuriisurzhykov.pointdetector.core

import android.content.res.Resources
import org.xmlpull.v1.XmlPullParser

private const val MAP_TAG = "map"
private const val ENTRY_TAG = "entry"
private const val KEY_ATTRIBUTE = "key"
private const val LINKED_ATTRIBUTE = "linked"

/**
 * Reads an xml resource of the form `<map linked="true"><entry key="a">text</entry></map>` into a
 * map; null when the resource is not of that form. `linked` keeps the order of the entries.
 */
fun Resources.getHashMapResource(hashMapResId: Int): Map<String, String>? {
    val parser = getXml(hashMapResId)
    try {
        var map: MutableMap<String, String>? = null
        var key: String? = null
        var value: String? = null
        var event = parser.eventType
        while (event != XmlPullParser.END_DOCUMENT) {
            when (event) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    MAP_TAG -> map = if (parser.getAttributeBooleanValue(null, LINKED_ATTRIBUTE, false)) LinkedHashMap() else HashMap()
                    ENTRY_TAG -> key = parser.getAttributeValue(null, KEY_ATTRIBUTE) ?: return null
                }

                XmlPullParser.TEXT -> if (key != null) value = parser.text

                XmlPullParser.END_TAG -> if (parser.name == ENTRY_TAG) {
                    val entryKey = key ?: return null
                    map?.put(entryKey, value.orEmpty())
                    key = null
                    value = null
                }
            }
            event = parser.next()
        }
        return map
    } catch (e: Exception) {
        return null
    } finally {
        parser.close()
    }
}
