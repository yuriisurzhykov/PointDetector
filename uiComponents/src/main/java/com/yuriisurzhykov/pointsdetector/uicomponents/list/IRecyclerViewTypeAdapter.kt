package com.yuriisurzhykov.pointsdetector.uicomponents.list

interface IRecyclerViewTypeAdapter<T> {

    fun getHolderClassByType(viewType: Int): Class<out AbstractViewHolder<T>>
}