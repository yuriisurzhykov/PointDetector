package com.yuriisurzhykov.pointdetector.di

import com.yuriisurzhykov.pointdetector.presentation.places.help.AppointmentFactFactory
import com.yuriisurzhykov.pointdetector.presentation.places.help.DocumentsFactFactory
import com.yuriisurzhykov.pointdetector.presentation.places.help.HelpFactFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

/** Every kind of fact about how to get help. A new kind is one class and one line here. */
@Module
@InstallIn(SingletonComponent::class)
interface HelpFactsModule {

    @Binds
    @IntoSet
    fun appointment(factory: AppointmentFactFactory): HelpFactFactory

    @Binds
    @IntoSet
    fun documents(factory: DocumentsFactFactory): HelpFactFactory
}
