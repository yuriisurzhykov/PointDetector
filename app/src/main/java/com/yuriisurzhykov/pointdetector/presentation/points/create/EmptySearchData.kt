package com.yuriisurzhykov.pointdetector.presentation.points.create

import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderItem

class EmptySearchData : ViewHolderItem.Abstract() {
    override fun areContentsTheSame(other: Any): Boolean {
        return areItemsTheSame(other)
    }
}