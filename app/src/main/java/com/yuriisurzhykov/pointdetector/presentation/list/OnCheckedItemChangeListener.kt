package com.yuriisurzhykov.pointdetector.presentation.list

fun interface OnCheckedItemChangeListener<T> {
    fun onCheckedChange(item: T)
}