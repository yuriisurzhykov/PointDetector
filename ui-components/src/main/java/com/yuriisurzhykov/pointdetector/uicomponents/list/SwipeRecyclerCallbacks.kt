package com.yuriisurzhykov.pointdetector.uicomponents.list

import androidx.recyclerview.widget.RecyclerView

interface SwipeRecyclerCallbacks<T : Any> {

    fun onStartSwipe(viewHolder: RecyclerView.ViewHolder) {}
    fun onSwipeReleased(viewHolder: RecyclerView.ViewHolder) {}
    fun onSwipedToLeft(viewHolder: RecyclerView.ViewHolder, position: Int, item: T) {}
    fun onSwipedToRight(viewHolder: RecyclerView.ViewHolder, position: Int, item: T) {}
}