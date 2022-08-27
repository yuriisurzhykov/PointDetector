package com.yuriisurzhykov.pointdetector.domain.source

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface DateFormatterType {

    fun getFormatter(): SimpleDateFormat

    abstract class Abstract(format: String) : DateFormatterType {

        private val formatter = SimpleDateFormat(format, Locale.getDefault())

        override fun getFormatter(): SimpleDateFormat = formatter
    }

    @Singleton
    class OnlyWeekDayFormat @Inject constructor() : Abstract("EEE")

    @Singleton
    class TimeFormat @Inject constructor() : Abstract("hh:mm")
}