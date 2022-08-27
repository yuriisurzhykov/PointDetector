package com.yuriisurzhykov.pointdetector.domain.services

import com.yuriisurzhykov.pointdetector.core.Mapper
import javax.inject.Inject

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