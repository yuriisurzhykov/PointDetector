package com.yuriisurzhykov.pointdetector.domain.services

import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.domain.source.DateFormatterType
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class DayStringToListConverter @Inject constructor(
    private val weekDayResource: WeekDaysListResource
) : Mapper<String, List<String>> {

    override fun map(from: String): List<String> {
        val resultList = mutableListOf<String>()
        val fullList = weekDayResource.getDaysAsList()
        val splitted = from.split("-")
        var firstFound = false
        fullList.forEach {
            if (it == splitted.first()) {
                firstFound = true
            }
            if (firstFound) {
                resultList.add(it)
            }
            if (it == splitted.last()) {
                return@forEach
            }
        }
        return resultList
    }
}

interface WeekDaysListResource {
    fun getDaysAsList(): List<String>

    @Singleton
    class Base @Inject constructor(
        private val dayTypeFormatter: DateFormatterType.OnlyWeekDayFormat
    ) : WeekDaysListResource {

        private val daysList by lazy { initDaysList() }

        override fun getDaysAsList(): List<String> = daysList

        private fun initDaysList(): List<String> {
            val resultList = mutableListOf<String>()
            val calendar = Calendar.getInstance()
            val minDay = calendar.getActualMinimum(Calendar.DAY_OF_WEEK)
            val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_WEEK)
            calendar.set(Calendar.DAY_OF_WEEK, minDay)
            for (i in minDay until maxDay) {
                resultList.add(dayTypeFormatter.getFormatter().format(calendar.get(Calendar.DAY_OF_WEEK)))
                calendar.add(Calendar.DAY_OF_WEEK, 1)
            }
            return resultList
        }
    }
}