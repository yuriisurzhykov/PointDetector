package com.yuriisurzhykov.pointdetector.presentation.delegate

import kotlin.properties.ReadOnlyProperty

interface ReadOnlyClearProperty<in T : Any, out V : Any> : ReadOnlyProperty<T, V> {

    fun clear()
}