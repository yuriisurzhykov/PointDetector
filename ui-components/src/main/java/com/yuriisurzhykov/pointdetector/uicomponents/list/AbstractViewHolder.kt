package com.yuriisurzhykov.pointdetector.uicomponents.list

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractViewHolder<out T>(view: View, shouldSetClickListener: Boolean = true) : RecyclerView.ViewHolder(view) {

    private var item: T? = null
    private var onItemClickListener: OnItemClickListener<T>? = null
    private var adapter: RecyclerView.Adapter<*>? = null

    init {
        if (shouldSetClickListener) {
            itemView.setOnClickListener {
                item?.let { item -> onItemClickListener?.onItemClick(item) }
            }
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
    fun onDetachedFromRecycler() {
        adapter = null
    }

}