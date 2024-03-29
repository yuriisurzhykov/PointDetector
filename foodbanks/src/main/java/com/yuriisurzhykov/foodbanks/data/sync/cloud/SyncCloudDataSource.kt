package com.yuriisurzhykov.foodbanks.data.sync.cloud

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface SyncCloudDataSource {

    suspend fun cityUpdateTime(): Long

    abstract class Abstract(
        private val firebaseDatabase: FirebaseDatabase
    ) : SyncCloudDataSource {

        override suspend fun cityUpdateTime(): Long {
            return if (firebaseDatabase.getReference("sync_info").get().await().exists()) 1 else 0
        }
    }

    class Base @Inject constructor(database: FirebaseDatabase) : Abstract(database)
}