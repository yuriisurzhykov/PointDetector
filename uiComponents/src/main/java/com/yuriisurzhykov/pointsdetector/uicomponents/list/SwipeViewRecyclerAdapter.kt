package com.yuriisurzhykov.pointsdetector.uicomponents.list

open class SwipeViewRecyclerAdapter(viewHolderTypeManager: ViewHolderTypeManager<ViewHolderItem>) :
    BaseRecyclerViewAdapter(viewHolderTypeManager) {

    private var onSwipedCallback: ((item: ViewHolderItem, position: Int) -> Unit)? = null

    fun setOnSwipeListener(listener: (item: ViewHolderItem, position: Int) -> Unit) {
        onSwipedCallback = listener
    }

    override fun onItemRemoved(position: Int, item: ViewHolderItem) {
        super.onItemRemoved(position, item)
        onSwipedCallback?.invoke(item, position)
    }
}