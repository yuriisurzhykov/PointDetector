package com.yuriisurzhykov.pointdetector.uicomponents.list

interface IRecyclerViewTypeAdapter<T> {

    fun getHolderClassByType(viewType: Int): Class<out AbstractViewHolder<T>>
}