package com.yuriisurzhykov.pointdetector.uicomponents.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

interface ViewHolderFactory<VH : RecyclerView.ViewHolder> {

    fun create(clazz: Class<out RecyclerView.ViewHolder>, parent: ViewGroup): VH
}

