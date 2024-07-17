package com.yuriisurzhykov.pointdetector.uicomponents.list

import androidx.recyclerview.widget.DiffUtil

class BaseDiffCallback : DiffUtil.ItemCallback<ViewHolderItem>() {
    override fun areItemsTheSame(oldItem: ViewHolderItem, newItem: ViewHolderItem): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: ViewHolderItem, newItem: ViewHolderItem): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}