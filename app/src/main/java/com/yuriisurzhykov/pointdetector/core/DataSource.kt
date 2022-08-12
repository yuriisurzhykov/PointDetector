package com.yuriisurzhykov.pointdetector.core

import kotlinx.coroutines.flow.Flow

interface DataSource<E> {

    suspend fun data(): Flow<E>

}