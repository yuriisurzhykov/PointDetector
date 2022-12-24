package com.yuriisurzhykov.pointsdetector.uicomponents.list

import androidx.recyclerview.widget.RecyclerView

interface SwipeRecyclerCallbacks<T : Any> {

    fun onStartSwipe(viewHolder: RecyclerView.ViewHolder) {}
    fun onSwipeReleased(viewHolder: RecyclerView.ViewHolder) {}
    fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int, item: T) {}
}