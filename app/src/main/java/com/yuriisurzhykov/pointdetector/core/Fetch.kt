package com.yuriisurzhykov.pointdetector.core

interface Fetch<E> {

    suspend fun fetch(): E

}

interface ConditionFetch<C, E> {

    suspend fun fetchByCondition(condition: C): E

}