package com.yuriisurzhykov.pointsdetector.uicomponents.list

fun interface OnItemClickListener<in T> {

    fun onItemClick(item: T)

}