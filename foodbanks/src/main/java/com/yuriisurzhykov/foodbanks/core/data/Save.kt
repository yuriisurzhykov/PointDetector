package com.yuriisurzhykov.foodbanks.core.data

interface Save<T : Any> {

    suspend fun save(value: T)
}