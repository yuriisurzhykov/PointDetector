package com.yuriisurzhykov.pointdetector.uicomponents.list

class EmptyStateData : ViewHolderItem.Abstract() {
    override fun areContentsTheSame(other: Any): Boolean {
        return other == this
    }
}