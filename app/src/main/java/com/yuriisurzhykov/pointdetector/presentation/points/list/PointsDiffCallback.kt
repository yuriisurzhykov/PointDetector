package com.yuriisurzhykov.pointdetector.presentation.points.list

import androidx.recyclerview.widget.DiffUtil
import com.yuriisurzhykov.pointdetector.domain.entities.Point

class PointsDiffCallback : DiffUtil.ItemCallback<Point>() {

    override fun areItemsTheSame(oldItem: Point, newItem: Point): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Point, newItem: Point): Boolean {
        return oldItem.address == newItem.address
    }
}