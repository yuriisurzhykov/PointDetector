package com.yuriisurzhykov.pointdetector.uicomponents.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolderFactory<VH : RecyclerView.ViewHolder>(
    private val inflater: LayoutInflater
) : ViewHolderFactory<VH> {

    @Suppress("UNCHECKED_CAST")
    override fun create(holderContainer: ViewHolderContainer<*>, parent: ViewGroup): VH {
        val view = inflater.inflate(holderContainer.layoutId, parent, false)
        return holderContainer.javaClass.getConstructor(View::class.java).newInstance(view) as VH
    }
}