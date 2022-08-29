package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.view.View
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.entities.Distance
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolder
import com.yuriisurzhykov.pointsdetector.uicomponents.list.AbstractViewHolder
import com.yuriisurzhykov.pointsdetector.uicomponents.list.OnItemClickListener

@ViewHolder(R.layout.list_item_empty_points_list)
class EmptyPointViewHolder(view: View) : AbstractViewHolder<Point>(view) {

    private val button by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById<TextView>(R.id.empty_state_button)
    }

    override fun bind(item: Point, clickListener: OnItemClickListener<Point>?) {
        super.bind(item, clickListener)
        button.setText(R.string.button_create_new_point_label)
        button.setOnClickListener {
            clickListener?.onItemClick(item)
        }
    }

}

@ViewHolder(R.layout.list_item_point_2_0)
class PointsViewHolder(view: View) : AbstractViewHolder<Point>(view) {

    private val addressText: TextView by lazy { itemView.findViewById(R.id.location_address) }
    private val addressName: TextView by lazy { itemView.findViewById(R.id.location_name) }
    private val distanceText: TextView by lazy { itemView.findViewById(R.id.location_distance) }
    private val availabilityState: TextView by lazy { itemView.findViewById(R.id.availability_state) }

    override fun bind(
        item: Point, clickListener: OnItemClickListener<Point>?
    ) {
        super.bind(item, clickListener)
        addressText.text = item.address
        addressName.text = item.placeName
        distanceText.text = Distance.Kilometers(item.distance).getDistanceLocale(itemView.context)
        availabilityState.isEnabled = item.isPointAvailable
        if (item.isPointAvailable) {
            availabilityState.text = itemView.context.getString(R.string.label_point_open)
        } else {
            availabilityState.text = itemView.context.getString(R.string.label_point_closed)
        }
        if (clickListener != null) {
            itemView.setOnClickListener { clickListener.onItemClick(item) }
        }
    }
}