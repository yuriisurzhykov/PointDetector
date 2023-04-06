package com.yuriisurzhykov.foodbanks.data.sync

import com.yuriisurzhykov.foodbanks.data.sync.cache.SyncCacheDataSource

interface SyncRepository {

    suspend fun performSync()

    abstract class Abstract(
        private val syncCacheDataSource: SyncCacheDataSource,

        )

}