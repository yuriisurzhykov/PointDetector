package com.yuriisurzhykov.pointsdetector.uicomponents.list

open class BaseRecyclerViewAdapter(viewHolderTypeManager: ViewHolderTypeManager<ViewHolderItem>) :
    AbstractRecyclerAdapter<ViewHolderItem, AbstractViewHolder<ViewHolderItem>>(
        BaseDiffCallback(),
        viewHolderTypeManager
    )