package com.yuriisurzhykov.foodbanks.data.sync.cache

import javax.inject.Inject

interface SyncCacheDataSource {

    suspend fun lastSyncTime(): Long

    abstract class Abstract(
        private val syncDao: SyncDao
    ) : SyncCacheDataSource {

        override suspend fun lastSyncTime() = syncDao.lastUpdate().lastSyncTime
    }

    class Base @Inject constructor(dao: SyncDao) : Abstract(dao)
}