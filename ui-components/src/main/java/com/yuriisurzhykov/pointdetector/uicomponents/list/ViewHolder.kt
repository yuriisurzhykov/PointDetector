package com.yuriisurzhykov.pointdetector.uicomponents.list

import androidx.annotation.LayoutRes
import androidx.annotation.Nullable

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewHolder(@LayoutRes @Nullable val layoutRes: Int = 0)