package com.yuriisurzhykov.pointdetector.uicomponents.textselect

fun interface OnItemSelectListener<T> {
    fun onItemSelected(item: T): Boolean
}