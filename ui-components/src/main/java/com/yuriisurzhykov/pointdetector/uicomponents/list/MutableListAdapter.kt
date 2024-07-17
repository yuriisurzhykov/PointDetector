package com.yuriisurzhykov.pointdetector.uicomponents.list

interface MutableListAdapter<T> {
    fun submitList(list: List<T>?)
    fun removeItem(item: T)
    fun removeItem(position: Int)
    fun insertItem(item: T, position: Int)
    fun getCurrentList(): List<T>
}