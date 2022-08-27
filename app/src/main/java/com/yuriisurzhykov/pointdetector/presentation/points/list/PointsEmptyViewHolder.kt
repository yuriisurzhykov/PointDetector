package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.OnItemClickListener
import com.yuriisurzhykov.pointdetector.presentation.list.AbstractViewHolder

class PointsEmptyViewHolder(view: View) : AbstractViewHolder<Point>(view) {

    private val button: Button by lazy { itemView.findViewById(R.id.empty_state_button) }

    override fun bind(item: Point, clickListener: OnItemClickListener<Point>?) {
        button.text = itemView.context.getString(R.string.button_empty_state_add_record)
        button.setOnClickListener {
            clickListener?.onItemClick(item)
        }
    }

    companion object {
        fun create(parent: ViewGroup, inflater: LayoutInflater): PointsEmptyViewHolder {
            return PointsEmptyViewHolder(
                inflater.inflate(
                    R.layout.list_item_empty_points_list, parent, false
                )
            )
        }
    }
}