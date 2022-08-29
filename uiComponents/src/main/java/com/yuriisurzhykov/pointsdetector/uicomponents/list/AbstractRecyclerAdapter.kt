package com.yuriisurzhykov.pointsdetector.uicomponents.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class AbstractRecyclerAdapter<T : Any, VH : AbstractViewHolder<T>>(
    callback: DiffUtil.ItemCallback<T>,
    private val viewHolderTypeManager: ViewHolderTypeManager<T>
) : ListAdapter<T, VH>(callback), IRecyclerViewTypeAdapter<T> {

    private val dataList = mutableListOf<T>()

    private var onItemClickListener: OnItemClickListener<T>? = null
    private var inflater: LayoutInflater? = null

    override fun getItemViewType(position: Int): Int {
        return viewHolderTypeManager.getViewHolderType(getItem(position))
    }

    override fun getHolderClassByType(viewType: Int): Class<out AbstractViewHolder<T>> {
        return viewHolderTypeManager.getViewHolderClass(viewType)
    }

    open fun onItemRemoved(position: Int, item: T) {
        notifyItemRemoved(position)
    }

    override fun submitList(list: List<T>?) {
        dataList.clear()
        dataList.addAll(list.orEmpty())
        super.submitList(list)
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        onItemClickListener = listener
        submitList(dataList)
    }

    fun removeItem(position: Int) {
        val itemToRemove = getItem(position)
        dataList.remove(itemToRemove)
        onItemRemoved(position, itemToRemove)
        submitList(dataList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return BaseViewHolderFactory<VH>(layoutInflater(parent.context)).create(
            getHolderClassByType(viewType), parent
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        holder.setOnClickListener(onItemClickListener)
        holder.onAttachedToRecycler(this)
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetachedFromRecycler(this)
    }

    private fun layoutInflater(context: Context): LayoutInflater {
        if (inflater == null) {
            inflater = LayoutInflater.from(context)
        }
        return inflater!!
    }
}