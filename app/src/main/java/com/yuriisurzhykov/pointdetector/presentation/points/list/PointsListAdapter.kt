package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.OnItemClickListener

open class PointsListAdapter : ListAdapter<Point, PointsViewHolder>(PointsDiffCallback()) {

    private var onItemClickListener: OnItemClickListener<Point>? = null
    private var onItemRemoveListener: ((Point, Int) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setOnItemClickListener(listener: OnItemClickListener<Point>) {
        onItemClickListener = listener
        notifyDataSetChanged()
    }

    fun setOnRemoveClickListener(listener: (Point, Int) -> Unit) {
        onItemRemoveListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        return PointsViewHolder.newInstance(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    fun removeItem(adapterPosition: Int) {
        val itemToRemove = getItem(adapterPosition)
        onItemRemoveListener?.invoke(itemToRemove, adapterPosition)
        val list = getItemsList()
        list.remove(itemToRemove)
        submitList(list)
    }

    private fun getItemsList(): ArrayList<Point> {
        val differField = ListAdapter::class.java.getDeclaredField("mDiffer")
        differField.isAccessible = true
        return ArrayList((differField.get(this) as AsyncListDiffer<Point>).currentList)
    }
}