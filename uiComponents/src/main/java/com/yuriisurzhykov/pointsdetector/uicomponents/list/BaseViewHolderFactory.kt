package com.yuriisurzhykov.pointsdetector.uicomponents.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolderFactory<VH : RecyclerView.ViewHolder>(
    private val inflater: LayoutInflater
) : ViewHolderFactory<VH> {

    @Suppress("UNCHECKED_CAST")
    override fun create(clazz: Class<out RecyclerView.ViewHolder>, parent: ViewGroup): VH {
        if (clazz.isAnnotationPresent(ViewHolder::class.java)) {
            val annotation = clazz.annotations.find { it is ViewHolder } as? ViewHolder
            if (annotation != null && AbstractViewHolder::class.java.isAssignableFrom(clazz)) {
                val view = inflater.inflate(annotation.layoutRes, parent, false)
                return clazz.getConstructor(View::class.java).newInstance(view) as VH
            }
        }
        throw IllegalArgumentException("Illegal $clazz! You must to annotate it with @ViewHolder annotation!")
    }
}