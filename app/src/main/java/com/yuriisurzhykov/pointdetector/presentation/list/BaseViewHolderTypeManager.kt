package com.yuriisurzhykov.pointdetector.presentation.list

import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.presentation.points.create.EmptySearchData
import com.yuriisurzhykov.pointdetector.presentation.points.create.EmptySearchViewHolder
import com.yuriisurzhykov.pointdetector.presentation.points.create.PointSimplifiedViewHolder
import com.yuriisurzhykov.pointdetector.presentation.points.details.WorkingHoursViewHolder
import com.yuriisurzhykov.pointdetector.presentation.points.list.EmptyPointViewHolder
import com.yuriisurzhykov.pointdetector.presentation.points.list.PointsViewHolder
import com.yuriisurzhykov.pointdetector.uicomponents.list.EmptyStateData
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderContainer
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderItem
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderTypeManager
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay

open class BaseViewHolderTypeManager : ViewHolderTypeManager.Abstract<ViewHolderItem>() {

    override fun getViewHolderTypeWithClass(item: ViewHolderItem): ViewHolderContainer<ViewHolderItem> {
        return when (item) {
            is StartSearchData -> ViewHolderContainer(
                1,
                StartSearchViewHolder::class,
                R.layout.list_item_empty_points_list
            )

            is EmptyStateData -> ViewHolderContainer(
                2,
                EmptyPointViewHolder::class,
                R.layout.list_item_empty_points_list
            )

            is PointUi -> ViewHolderContainer(
                3,
                PointsViewHolder::class,
                R.layout.list_item_point_2_0
            )

            is EmptySearchData -> ViewHolderContainer(
                4,
                EmptySearchViewHolder::class,
                R.layout.list_item_entered_data_empty
            )

            is WeekDay -> ViewHolderContainer(
                5,
                WorkingHoursViewHolder::class,
                R.layout.list_item_week_day
            )

            is Point -> ViewHolderContainer(
                6,
                PointSimplifiedViewHolder::class,
                R.layout.list_item_point
            )

            else -> throw IllegalStateException("Unresolved item $item! There is no view provided view holder for this item.")
        }
    }
}