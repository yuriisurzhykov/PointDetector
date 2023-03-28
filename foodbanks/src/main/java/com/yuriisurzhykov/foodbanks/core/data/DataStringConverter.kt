package com.yuriisurzhykov.foodbanks.core.data

import com.google.gson.Gson
import javax.inject.Inject

interface DataStringConverter {

    fun map(input: Any): String

    fun inverseMap(value: String, clazz: Class<*>): Any

    class Base @Inject constructor(
        private val gson: Gson
    ) : DataStringConverter {

        override fun map(input: Any): String = gson.toJson(input)

        override fun inverseMap(value: String, clazz: Class<*>): Any =
            gson.fromJson(value, clazz)
    }
}