package com.yuriisurzhykov.pointsdetector.uicomponents.list

import androidx.recyclerview.widget.DiffUtil

abstract class AbstractDiffCallback<T>(private val oldItems: List<T>, private val newItems: List<T>) :
    DiffUtil.Callback() {

    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])
    }
}