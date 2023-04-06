package com.yuriisurzhykov.foodbanks

import android.app.Instrumentation
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.yuriisurzhykov.foodbanks.data.sync.cloud.SyncCloudDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class SyncCloudDataSourceTest {

    @Test
    fun `test cloud data source`(): Unit = runBlocking {
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().context.applicationContext)
        SyncCloudDataSource.Base(FirebaseDatabase.getInstance()).cityUpdateTime()
    }
}