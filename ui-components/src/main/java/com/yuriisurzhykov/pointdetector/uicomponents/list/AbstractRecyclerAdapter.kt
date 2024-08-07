package com.yuriisurzhykov.pointdetector.uicomponents.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractRecyclerAdapter<T : Any, VH : AbstractViewHolder<T>>(
    private val viewHolderTypeManager: ViewHolderTypeManager<T>
) : RecyclerView.Adapter<VH>(),
    IRecyclerViewTypeAdapter<T>,
    SwipeRecyclerCallbacks<T>,
    MutableListAdapter<T> {

    private var onItemClickListener: OnItemClickListener<T>? =
        null
    private var inflater: LayoutInflater? = null
    private val dataList = mutableListOf<T>()

    abstract fun getDiffCallback(oldItems: List<T>, newItems: List<T>): DiffUtil.Callback

    override fun getItemViewType(position: Int): Int {
        return viewHolderTypeManager.getViewHolderType(getItem(position))
    }

    override fun getViewHolderGenerationInfo(viewType: Int): ViewHolderContainer<T> {
        return viewHolderTypeManager.getViewHolderClass(viewType)
    }

    open fun onItemRemoved(holder: RecyclerView.ViewHolder, position: Int, item: T) {
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        onItemClickListener = listener
    }

    fun removeItem(holder: RecyclerView.ViewHolder, position: Int) {
        val itemToRemove = getItem(position)
        onSwipedToLeft(holder, position, itemToRemove)
    }

    override fun removeItem(item: T) {
        val itemPosition = dataList.indexOf(item)
        if (itemPosition != RecyclerView.NO_POSITION) {
            removeItem(itemPosition)
        }
    }

    override fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun insertItem(item: T, position: Int) {
        dataList.add(position, item)
        notifyItemInserted(position)
    }

    override fun getCurrentList(): MutableList<T> = dataList

    override fun submitList(list: List<T>?) {
        val callback = getDiffCallback(dataList, list.orEmpty())
        DiffUtil.calculateDiff(callback, true).dispatchUpdatesTo(this)
        dataList.clear()
        dataList.addAll(list.orEmpty())
    }

    override fun onSwipedToLeft(viewHolder: RecyclerView.ViewHolder, position: Int, item: T) {
        removeItem(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return BaseViewHolderFactory<VH>(layoutInflater(parent.context))
            .create(getViewHolderGenerationInfo(viewType), parent)
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
        holder.onDetachedFromRecycler()
    }

    override fun getItemCount(): Int = dataList.size

    fun getItem(position: Int): T {
        return dataList[position]
    }

    private fun layoutInflater(context: Context): LayoutInflater {
        if (inflater == null) {
            inflater = LayoutInflater.from(context)
        }
        return inflater!!
    }
}