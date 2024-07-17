package com.yuriisurzhykov.pointdetector.presentation.points.details

import android.view.View
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.uicomponents.list.AbstractViewHolder
import com.yuriisurzhykov.pointdetector.uicomponents.list.OnItemClickListener
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolder
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolderItem
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay

@ViewHolder(R.layout.list_item_week_day)
class WorkingHoursViewHolder(view: View) : AbstractViewHolder<ViewHolderItem>(view) {

    private val dayView: TextView by lazy { itemView.findViewById(android.R.id.text1) }

    override fun bind(item: ViewHolderItem, clickListener: OnItemClickListener<ViewHolderItem>?) {
        super.bind(item, clickListener)
        item as WeekDay
        dayView.text = itemView.context.getString(R.string.label_format_working_days)
            .format(item.dayName, item.hoursFrom, item.hoursTo)
    }

}