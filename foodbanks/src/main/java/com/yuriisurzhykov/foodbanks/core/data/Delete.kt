package com.yuriisurzhykov.foodbanks.core.data

interface Delete<T : Any> {

    suspend fun delete(value: T)
}