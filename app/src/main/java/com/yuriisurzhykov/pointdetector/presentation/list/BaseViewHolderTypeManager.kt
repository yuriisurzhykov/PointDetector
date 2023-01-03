package com.yuriisurzhykov.pointdetector.presentation.list

import androidx.core.util.Pair
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.points.create.EmptySearchData
import com.yuriisurzhykov.pointdetector.presentation.points.create.EmptySearchViewHolder
import com.yuriisurzhykov.pointdetector.presentation.points.create.PointSimplifiedViewHolder
import com.yuriisurzhykov.pointdetector.presentation.points.details.WorkingHoursViewHolder
import com.yuriisurzhykov.pointdetector.presentation.points.list.EmptyPointViewHolder
import com.yuriisurzhykov.pointsdetector.uicomponents.list.EmptyStateData
import com.yuriisurzhykov.pointdetector.presentation.points.list.PointsViewHolder
import com.yuriisurzhykov.pointsdetector.uicomponents.list.AbstractViewHolder
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderTypeManager
import com.yuriisurzhykov.pointsdetector.uicomponents.workday.entity.WeekDay

open class BaseViewHolderTypeManager : ViewHolderTypeManager.Abstract<ViewHolderItem>() {

    override fun getViewHolderTypeWithClass(item: ViewHolderItem): Pair<Int, Class<out AbstractViewHolder<ViewHolderItem>>> {
        return when (item) {
            is StartSearchData -> Pair(1, StartSearchViewHolder::class.java)
            is EmptyStateData -> Pair(2, EmptyPointViewHolder::class.java)
            is PointUi -> Pair(3, PointsViewHolder::class.java)
            is EmptySearchData -> Pair(4, EmptySearchViewHolder::class.java)
            is WeekDay -> Pair(5, WorkingHoursViewHolder::class.java)
            is Point -> Pair(6, PointSimplifiedViewHolder::class.java)
            else -> throw IllegalStateException("Unresolved item $item! There is no view provided view holder for this item.")
        }
    }
}