package com.yuriisurzhykov.pointsdetector.uicomponents.list

import androidx.annotation.LayoutRes

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewHolder(@LayoutRes val layoutRes: Int)