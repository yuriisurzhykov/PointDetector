package com.yuriisurzhykov.pointsdetector.uicomponents.list

class HolderDiffUtilsCallback constructor(
    oldItems: List<ViewHolderItem>,
    newItems: List<ViewHolderItem>
) : AbstractDiffCallback<ViewHolderItem>(oldItems, newItems) {
    override fun areContentsTheSame(oldItem: ViewHolderItem, newItem: ViewHolderItem): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

    override fun areItemsTheSame(oldItem: ViewHolderItem, newItem: ViewHolderItem): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }
}