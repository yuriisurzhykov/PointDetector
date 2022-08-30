package com.yuriisurzhykov.pointdetector.data.cache.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.yuriisurzhykov.pointdetector.data.cache.entities.WorkingHoursGroupCache
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
object WorkingHoursListConverter {
    @TypeConverter
    @JvmStatic
    fun stringToWorkingHoursList(input: String): WorkingHoursGroupCache {
        return if (input.isNotEmpty()) Json.decodeFromString(input) else WorkingHoursGroupCache(emptyList())
    }

    @TypeConverter
    @JvmStatic
    fun workingHoursListToString(list: WorkingHoursGroupCache): String {
        return Json.encodeToString(list)
    }
}