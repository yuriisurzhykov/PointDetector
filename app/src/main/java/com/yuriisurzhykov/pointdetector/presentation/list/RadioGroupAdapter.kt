package com.yuriisurzhykov.pointdetector.presentation.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import com.yuriisurzhykov.pointsdetector.uicomponents.list.MutableListAdapter
import java.lang.ref.SoftReference

class RadioGroupAdapter<T> constructor(
    private val radioButtonProvider: RadioButtonProvider<T>
) : MutableListAdapter<T> {

    var checkedChangeListener: OnCheckedItemChangeListener<T>? = null

    private val itemList = ArrayList<T>()
    private var radioGroup: SoftReference<RadioGroup> = SoftReference(null)

    override fun submitList(list: List<T>?) {
        itemList.clear()
        if (list != null) {
            itemList.addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun removeItem(item: T) {
        itemList.remove(item)
        notifyDataSetChanged()
    }

    override fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun insertItem(item: T, position: Int) {
        itemList.add(position, item)
        notifyDataSetChanged()
    }

    override fun getCurrentList() = ArrayList(itemList)

    fun attachToGroup(radioGroup: RadioGroup) {
        this.radioGroup.clear()
        this.radioGroup = SoftReference(radioGroup)
        notifyDataSetChanged()
    }

    @SuppressLint("ResourceType")
    private fun notifyDataSetChanged() {
        val parent: RadioGroup = radioGroup.get() ?: return
        parent.removeAllViews()
        parent.setOnCheckedChangeListener(null)
        val inflater = LayoutInflater.from(parent.context)
        itemList.forEachIndexed { index, item ->
            val button = radioButtonProvider.provideButton(item, parent, inflater)
            button.id = index + 1
            parent.addView(button)
        }
        if (parent.checkedRadioButtonId == -1) {
            parent.check(1)
        }
        parent.setOnCheckedChangeListener(internalCheckedChangeListener)
    }

    private val internalCheckedChangeListener = OnCheckedChangeListener { _, checkedId ->
        val item = itemList[checkedId - 1]
        checkedChangeListener?.onCheckedChange(item)
    }
}