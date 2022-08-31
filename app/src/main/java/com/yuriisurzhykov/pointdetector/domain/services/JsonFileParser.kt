package com.yuriisurzhykov.pointdetector.domain.services

interface JsonFileParser<T> {

    fun parse(fileName: String, clazz: Class<out T>): T
}