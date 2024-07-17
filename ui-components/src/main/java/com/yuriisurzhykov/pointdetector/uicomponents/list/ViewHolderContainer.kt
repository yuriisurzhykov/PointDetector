package com.yuriisurzhykov.pointdetector.uicomponents.list

import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

data class ViewHolderContainer<T>(
    val type: Int,
    val clazz: KClass<out AbstractViewHolder<T>>,
    @LayoutRes val layoutId: Int
) {
    val javaClass: Class<out AbstractViewHolder<T>> = clazz.java
}