package com.yuriisurzhykov.pointdetector.presentation.points.list

import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointsdetector.uicomponents.list.AbstractRecyclerAdapter
import com.yuriisurzhykov.pointsdetector.uicomponents.list.AbstractViewHolder

open class PointsListAdapter :
    AbstractRecyclerAdapter<Point, AbstractViewHolder<Point>>(PointsDiffCallback(), PointViewHolderTypeManager()) {

    private var onItemRemoveListener: ((Point, Int) -> Unit)? = null

    fun setOnRemoveClickListener(listener: (Point, Int) -> Unit) {
        onItemRemoveListener = listener
    }

    override fun onItemRemoved(position: Int, item: Point) {
        super.onItemRemoved(position, item)
        onItemRemoveListener?.invoke(item, position)
    }
}