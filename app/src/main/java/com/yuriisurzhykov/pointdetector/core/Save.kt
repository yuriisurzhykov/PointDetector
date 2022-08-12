package com.yuriisurzhykov.pointdetector.core

interface Save<E> {

    suspend fun save(entity: E)

    suspend fun save(list: List<E>)

}