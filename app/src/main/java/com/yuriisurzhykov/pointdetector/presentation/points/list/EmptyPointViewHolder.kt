package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.view.View
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.uicomponents.list.AbstractViewHolder
import com.yuriisurzhykov.pointdetector.uicomponents.list.EmptyStateData
import com.yuriisurzhykov.pointdetector.uicomponents.list.OnItemClickListener
import com.yuriisurzhykov.pointdetector.uicomponents.list.ViewHolder

@ViewHolder(R.layout.list_item_empty_points_list)
class EmptyPointViewHolder(view: View) : AbstractViewHolder<EmptyStateData>(view, false) {

    private val button by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById<TextView>(android.R.id.button1)
    }
    private val title by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById<TextView>(android.R.id.text1)
    }
    private val description by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById<TextView>(android.R.id.text2)
    }

    override fun bind(item: EmptyStateData, clickListener: OnItemClickListener<EmptyStateData>?) {
        super.bind(item, clickListener)
        title.setText(R.string.title_empty_points_list)
        description.setText(R.string.description_empty_points_list)
        button.setText(R.string.button_create_new_point_label)
        button.setOnClickListener {
            clickListener?.onItemClick(item)
        }
    }

}