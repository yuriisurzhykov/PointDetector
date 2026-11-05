package com.yuriisurzhykov.pointdetector.presentation.filter

import com.yuriisurzhykov.pointdetector.data.cache.configs.getMeasureUnit
import java.util.Locale

/** What "Reset" returns to: the same values the configs fall back to when nothing is saved. */
object FilterDefaults {

    const val SORT = "sort_by_distance"
    const val SORT_BY_NAME = "sort_by_name"

    fun unit(locale: Locale = Locale.getDefault()): String = locale.getMeasureUnit()
}
