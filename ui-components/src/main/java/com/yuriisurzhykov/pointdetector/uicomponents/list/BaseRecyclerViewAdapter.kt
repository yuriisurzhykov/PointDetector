package com.yuriisurzhykov.pointdetector.uicomponents.list

import androidx.recyclerview.widget.DiffUtil

open class BaseRecyclerViewAdapter(viewHolderTypeManager: com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderTypeManager<ViewHolderItem>) :
    com.yuriisurzhykov.pointdetector.uicomponents.list.AbstractRecyclerAdapter<ViewHolderItem, AbstractViewHolder<ViewHolderItem>>(
        viewHolderTypeManager
    ) {

    override fun getDiffCallback(
        oldItems: List<ViewHolderItem>,
        newItems: List<ViewHolderItem>
    ): DiffUtil.Callback {
        return BaseDiffUtilCallback(oldItems, newItems)
    }
}