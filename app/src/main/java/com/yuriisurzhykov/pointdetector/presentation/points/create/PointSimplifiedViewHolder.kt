package com.yuriisurzhykov.pointdetector.presentation.points.create

import android.view.View
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.uicomponents.list.AbstractViewHolder
import com.yuriisurzhykov.pointdetector.uicomponents.list.OnItemClickListener

class PointSimplifiedViewHolder(view: View) : AbstractViewHolder<Point>(view) {

    private val title: TextView by lazy { itemView.findViewById(R.id.location_name) }
    private val address: TextView by lazy { itemView.findViewById(R.id.location_address) }

    override fun bind(item: Point, clickListener: OnItemClickListener<Point>?) {
        super.bind(item, clickListener)
        title.text = item.placeName
        address.text = item.address
    }

}