package com.yuriisurzhykov.pointdetector.core

interface Delete<E> {

    suspend fun delete(entity: E)

}