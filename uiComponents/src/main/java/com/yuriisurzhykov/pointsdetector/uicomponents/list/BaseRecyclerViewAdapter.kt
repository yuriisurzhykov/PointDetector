package com.yuriisurzhykov.pointsdetector.uicomponents.list

import androidx.recyclerview.widget.DiffUtil

open class BaseRecyclerViewAdapter(viewHolderTypeManager: ViewHolderTypeManager<ViewHolderItem>) :
    AbstractRecyclerAdapter<ViewHolderItem, AbstractViewHolder<ViewHolderItem>>(
        viewHolderTypeManager
    ) {

    override fun getDiffCallback(
        oldItems: List<ViewHolderItem>,
        newItems: List<ViewHolderItem>
    ): DiffUtil.Callback {
        return BaseDiffUtilCallback(oldItems, newItems)
    }
}