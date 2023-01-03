package com.yuriisurzhykov.pointdetector.domain.config

interface SortingTypeExecutor<T> {

    suspend fun performSort(items: Collection<T>): Collection<T>

}