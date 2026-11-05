package com.yuriisurzhykov.pointdetector.presentation.points.list

import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.list.BaseViewHolderTypeManager
import com.yuriisurzhykov.pointdetector.uicomponents.list.BaseRecyclerViewAdapter

class PointsListAdapter : BaseRecyclerViewAdapter(BaseViewHolderTypeManager()) {

    /**
     * The point at the position, or null when there is the empty state or no such position.
     */
    fun pointAt(position: Int): PointUi? = getCurrentList().getOrNull(position) as? PointUi
}
