package com.yuriisurzhykov.foodbanks.data.sync

interface SyncRepository {

    suspend fun performSync()

}