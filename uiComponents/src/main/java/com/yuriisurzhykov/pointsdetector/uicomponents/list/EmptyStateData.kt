package com.yuriisurzhykov.pointsdetector.uicomponents.list

class EmptyStateData : ViewHolderItem.Abstract() {
    override fun areContentsTheSame(other: Any): Boolean {
        return other == this
    }
}