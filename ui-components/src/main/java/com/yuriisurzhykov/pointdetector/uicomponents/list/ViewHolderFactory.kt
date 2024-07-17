package com.yuriisurzhykov.pointdetector.uicomponents.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ViewHolderFactory<VH : RecyclerView.ViewHolder> {

    fun create(holderContainer: ViewHolderContainer<*>, parent: ViewGroup): VH
}

