package com.yuriisurzhykov.pointsdetector.uicomponents.textselect

fun interface OnItemSelectListener<T> {
    fun onItemSelected(item: T): Boolean
}