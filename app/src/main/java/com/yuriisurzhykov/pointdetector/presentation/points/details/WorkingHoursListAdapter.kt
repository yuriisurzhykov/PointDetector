package com.yuriisurzhykov.pointdetector.presentation.points.details

import androidx.recyclerview.widget.DiffUtil
import com.yuriisurzhykov.pointdetector.presentation.list.BaseViewHolderTypeManager
import com.yuriisurzhykov.pointsdetector.uicomponents.list.AbstractRecyclerAdapter
import com.yuriisurzhykov.pointsdetector.uicomponents.list.HolderDiffUtilsCallback
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem

class WorkingHoursListAdapter :
    AbstractRecyclerAdapter<ViewHolderItem, WorkingHoursViewHolder>(BaseViewHolderTypeManager()) {

    override fun getDiffCallback(
        oldItems: List<ViewHolderItem>, newItems: List<ViewHolderItem>
    ): DiffUtil.Callback {
        return HolderDiffUtilsCallback(oldItems, newItems)
    }
}