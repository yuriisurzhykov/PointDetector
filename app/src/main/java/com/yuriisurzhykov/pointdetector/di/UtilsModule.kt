package com.yuriisurzhykov.pointdetector.di

import com.google.gson.Gson
import com.yuriisurzhykov.pointdetector.domain.services.WeekDaysListResource
import com.yuriisurzhykov.pointdetector.domain.source.DateFormatterType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideWeekDaysListResource(formatter: DateFormatterType.OnlyWeekDayFormat): WeekDaysListResource {
        return WeekDaysListResource.Base(formatter)
    }

}