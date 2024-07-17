package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi
import com.yuriisurzhykov.pointdetector.uicomponents.list.AbstractViewHolder
import com.yuriisurzhykov.pointdetector.uicomponents.list.OnItemClickListener
import com.yuriisurzhykov.pointdetector.uicomponents.list.SwipableViewHolder

class PointsViewHolder(view: View) : AbstractViewHolder<PointUi>(view), SwipableViewHolder {

    private val addressText: TextView by lazy { itemView.findViewById(R.id.location_address) }
    private val addressName: TextView by lazy { itemView.findViewById(R.id.location_name) }
    private val distanceText: TextView by lazy { itemView.findViewById(R.id.location_distance) }
    private val availabilityState: TextView by lazy { itemView.findViewById(R.id.availability_state) }
    private val favoriteStateDrawable: ImageView by lazy { itemView.findViewById(R.id.favorite_icon) }
    private var canSwipe = false

    override fun canSwipe() = canSwipe

    override fun bind(
        item: PointUi, clickListener: OnItemClickListener<PointUi>?
    ) {
        super.bind(item, clickListener)
        canSwipe = !item.isEmpty()
        addressText.text = item.address
        addressName.text = item.placeName
        distanceText.text = item.distanceString
        availabilityState.isEnabled = item.isPointAvailable
        if (item.isPointAvailable) {
            availabilityState.text = itemView.context.getString(R.string.label_point_open)
        } else {
            availabilityState.text = itemView.context.getString(R.string.label_point_closed)
        }
        if (clickListener != null) {
            itemView.setOnClickListener { clickListener.onItemClick(item) }
        }
        item.favoriteState.apply(favoriteStateDrawable)
    }
}