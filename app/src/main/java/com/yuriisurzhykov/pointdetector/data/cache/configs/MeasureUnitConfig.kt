package com.yuriisurzhykov.pointdetector.data.cache.configs

import com.google.gson.Gson
import com.yuriisurzhykov.pointdetector.data.repository.ConfigsRepository
import java.util.Locale
import javax.inject.Inject

fun Locale.getMeasureUnit(): String {
    return when (this.country.uppercase()) {
        "US", "LR", "MM" -> "miles"
        else -> "kilometers"
    }
}

class MeasureUnitConfig @Inject constructor(
    dataProvider: ConfigsRepository, gson: Gson
) : AbstractConfigService<String>(dataProvider, gson) {

    override fun getConfigName(): String = "measure_unit_type"

    override suspend fun getConfigValue(): String {
        return super.getConfigValue() ?: Locale.getDefault().getMeasureUnit()
    }
}