package com.yuriisurzhykov.pointsdetector.uicomponents.list

import androidx.recyclerview.widget.RecyclerView

open class SwipeViewRecyclerAdapter(viewHolderTypeManager: ViewHolderTypeManager<ViewHolderItem>) :
    BaseRecyclerViewAdapter(viewHolderTypeManager) {

    private var onSwipedCallback: SwipeRecyclerCallbacks<ViewHolderItem>? = null

    fun setOnSwipeListener(listener: SwipeRecyclerCallbacks<ViewHolderItem>) {
        onSwipedCallback = listener
    }

    override fun onStartSwipe(viewHolder: RecyclerView.ViewHolder) {
        super.onStartSwipe(viewHolder)
        onSwipedCallback?.onStartSwipe(viewHolder)
    }

    override fun onSwipeReleased(viewHolder: RecyclerView.ViewHolder) {
        super.onSwipeReleased(viewHolder)
        onSwipedCallback?.onSwipeReleased(viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int, item: ViewHolderItem) {
        super.onSwiped(viewHolder, position, item)
        onSwipedCallback?.onSwiped(viewHolder, position, item)
    }

    override fun onItemRemoved(holder: RecyclerView.ViewHolder, position: Int, item: ViewHolderItem) {
        super.onItemRemoved(holder, position, item)
        onSwipedCallback?.onSwiped(holder, position, item)
    }
}