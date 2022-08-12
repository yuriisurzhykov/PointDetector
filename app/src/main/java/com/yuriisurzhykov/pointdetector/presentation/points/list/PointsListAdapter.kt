package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.OnItemClickListener

open class PointsListAdapter : ListAdapter<Point, PointsViewHolder>(PointsDiffCallback()) {

    private var onItemClickListener: OnItemClickListener<Point>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setOnItemClickListener(listener: OnItemClickListener<Point>) {
        onItemClickListener = listener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        return PointsViewHolder.newInstance(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }
}