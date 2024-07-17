package com.yuriisurzhykov.pointdetector.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.uicomponents.textselect.SelectOption

interface RadioButtonProvider<T> {
    fun provideButton(item: T, parent: ViewGroup, inflater: LayoutInflater): RadioButton
}

open class SelectOptionsRadioButtonInflater : RadioButtonProvider<SelectOption> {
    override fun provideButton(
        item: SelectOption, parent: ViewGroup, inflater: LayoutInflater
    ): RadioButton {
        val button = inflater.inflate(R.layout.item_view_radio_button, parent, false) as RadioButton
        button.text = item.selectLabel
        button.isChecked = item.isSelected()
        button.tag = item
        return button
    }
}