package com.yuriisurzhykov.pointdetector.uicomponents.list

interface IRecyclerViewTypeAdapter<T> {

    fun getViewHolderGenerationInfo(viewType: Int): ViewHolderContainer<T>
}