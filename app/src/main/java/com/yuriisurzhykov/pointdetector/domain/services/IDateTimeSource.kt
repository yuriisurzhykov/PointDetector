package com.yuriisurzhykov.pointdetector.domain.services

import com.yuriisurzhykov.pointdetector.domain.source.DateFormatterType
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

interface IDateTimeSource {

    fun getTime(): String

    fun getDay(): String

    class LocalDateTimeSource @Inject constructor(
        private val dateFormatter: DateFormatterType.OnlyWeekDayFormat,
        private val timeFormatter: DateFormatterType.TimeFormat
    ) : IDateTimeSource {

        override fun getTime(): String {
            return timeFormatter.getFormatter().format(Date())
        }

        override fun getDay(): String {
            return dateFormatter.getFormatter().format(Date())
        }
    }

}