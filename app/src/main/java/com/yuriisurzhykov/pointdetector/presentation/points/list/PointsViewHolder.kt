package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.OnItemClickListener
import com.yuriisurzhykov.pointdetector.presentation.list.AbstractViewHolder

class PointsViewHolder(view: View) : AbstractViewHolder<Point>(view) {

    private val addressText: TextView by lazy { itemView.findViewById(R.id.address) }
    private val distanceText: TextView by lazy { itemView.findViewById(R.id.distance) }

    override fun bind(item: Point, clickListener: OnItemClickListener<Point>?) {
        addressText.text = item.address
        distanceText.text =
            String.format(itemView.context.getString(R.string.kilometers_format), item.distance)
        if (clickListener != null) {
            itemView.setOnClickListener { clickListener.onItemClick(item) }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(inflater: LayoutInflater, parent: ViewGroup): PointsViewHolder {
            return PointsViewHolder(inflater.inflate(R.layout.list_item_point, parent, false))
        }
    }

}