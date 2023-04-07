package com.yuriisurzhykov.foodbanks.di.domain

import android.content.Context
import com.yuriisurzhykov.foodbanks.data.ConnectivityCheck
import com.yuriisurzhykov.foodbanks.domain.network.AndroidConnectivityCheck
import com.yuriisurzhykov.foodbanks.domain.network.networkRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ComponentsModule {

    @Provides
    @Singleton
    fun provideConnectivityCheck(@ApplicationContext context: Context): ConnectivityCheck {
        return AndroidConnectivityCheck(context, networkRequest)
    }
}