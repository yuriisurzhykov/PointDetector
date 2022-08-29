package com.yuriisurzhykov.pointdetector.presentation.points.list

import androidx.core.util.Pair
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointsdetector.uicomponents.list.AbstractViewHolder
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderTypeManager

class PointViewHolderTypeManager : ViewHolderTypeManager.Abstract<Point>() {

    override fun getViewHolderTypeWithClass(item: Point): Pair<Int, Class<out AbstractViewHolder<Point>>> {
        return if (item.isEmpty()) {
            Pair(0, EmptyPointViewHolder::class.java)
        } else {
            Pair(1, PointsViewHolder::class.java)
        }
    }
}