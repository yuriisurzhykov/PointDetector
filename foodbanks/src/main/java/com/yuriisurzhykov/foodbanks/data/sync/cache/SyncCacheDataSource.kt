package com.yuriisurzhykov.foodbanks.data.sync.cache

interface SyncCacheDataSource {

    suspend fun lastSyncTime(): Long

    abstract class Abstract(
        private val syncDao: SyncDao
    ) : SyncCacheDataSource {

        override suspend fun lastSyncTime() = syncDao.lastUpdate().lastSyncTime
    }
}