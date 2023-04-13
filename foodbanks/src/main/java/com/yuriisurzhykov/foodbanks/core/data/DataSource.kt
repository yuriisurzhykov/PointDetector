package com.yuriisurzhykov.foodbanks.core.data

import kotlinx.coroutines.flow.Flow

interface DataSource<T : Any> {

    suspend fun data(): Flow<T>

}