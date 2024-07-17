package com.yuriisurzhykov.pointdetector.uicomponents.list

fun interface OnItemClickListener<in T> {

    fun onItemClick(item: T)

}