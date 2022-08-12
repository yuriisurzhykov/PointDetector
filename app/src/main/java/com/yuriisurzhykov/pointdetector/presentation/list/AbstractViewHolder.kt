package com.yuriisurzhykov.pointdetector.presentation.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yuriisurzhykov.pointdetector.presentation.core.OnItemClickListener

abstract class AbstractViewHolder<T : Any>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: T, clickListener: OnItemClickListener<T>?)

}