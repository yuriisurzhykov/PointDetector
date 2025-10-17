package com.yuriisurzhykov.foodbanks.di.data

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.yuriisurzhykov.foodbanks.data.FoodbankDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideFoodbankDatabase(@ApplicationContext context: Context): FoodbankDatabase {
        return Room
            .databaseBuilder(context, FoodbankDatabase::class.java, "foodbanks.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database
    }
}
