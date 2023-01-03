package com.yuriisurzhykov.pointdetector.presentation.core

import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import kotlin.properties.Delegates.notNull


fun Resources.getHashMapResource(hashMapResId: Int): Map<String, String>? {
    var map: MutableMap<String, String> by notNull()
    val parser: XmlResourceParser = getXml(hashMapResId)
    var key: String? = null
    var value: String? = null
    try {
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                Log.d("utils", "Start document")
            } else if (eventType == XmlPullParser.START_TAG) {
                if (parser.name == "map") {
                    val isLinked = parser.getAttributeBooleanValue(null, "linked", false)
                    map = if (isLinked) LinkedHashMap() else HashMap()
                } else if (parser.name == "entry") {
                    key = parser.getAttributeValue(null, "key")
                    if (null == key) {
                        parser.close()
                        return null
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.name == "entry") {
                    map[key!!] = value!!
                    key = null
                    value = null
                }
            } else if (eventType == XmlPullParser.TEXT) {
                if (null != key) {
                    value = parser.text
                }
            }
            eventType = parser.next()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
    return map
}