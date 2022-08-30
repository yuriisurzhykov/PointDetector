package com.yuriisurzhykov.pointsdetector.uicomponents.list

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractViewHolder<out T>(view: View) : RecyclerView.ViewHolder(view) {

    private var item: T? = null
    private var onItemClickListener: OnItemClickListener<T>? = null
    protected var adapter: RecyclerView.Adapter<*>? = null

    init {
        itemView.setOnClickListener {
            item?.let { item -> onItemClickListener?.onItemClick(item) }
        }
    }

    @CallSuper
    open fun setOnClickListener(onItemClickListener: OnItemClickListener<T>?) {
        this.onItemClickListener = onItemClickListener
    }

    @CallSuper
    open fun bind(item: @UnsafeVariance T, clickListener: OnItemClickListener<T>?) {
        this.item = item
    }

    @CallSuper
    open fun onAttachedToRecycler(recyclerView: RecyclerView.Adapter<*>) {
        adapter = recyclerView
    }

    @CallSuper
    fun onDetachedFromRecycler(recyclerView: RecyclerView.Adapter<*>) {
        adapter = null
    }

}