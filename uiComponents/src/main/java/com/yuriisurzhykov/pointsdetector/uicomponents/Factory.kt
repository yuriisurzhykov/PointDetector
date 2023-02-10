package com.yuriisurzhykov.pointsdetector.uicomponents

import android.content.Context

interface Factory<T : Any> {
    fun produce(): T
}

interface FactoryContext<T : Any> {
    fun produce(context: Context): T
}