package com.yuriisurzhykov.pointsdetector.uicomponents.textselect

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import com.yuriisurzhykov.pointsdetector.uicomponents.R

class TextPopupSelectView : AppCompatTextView {

    private val selectOptions = mutableListOf<SelectOption>()
    private val selectOptionsMenu: PopupMenu by lazy(LazyThreadSafetyMode.NONE) {
        PopupMenu(context, this, Gravity.RIGHT or Gravity.BOTTOM)
    }
    private var onOptionSelectedListener: OnItemSelectListener<SelectOption>? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    init {
        setOnClickListener { showOptions() }
    }

    fun setOptions(selectOptions: List<SelectOption>) {
        selectOptionsMenu.dismiss()
        this.selectOptions.clear()
        this.selectOptions.addAll(selectOptions)
        val selectedOption: SelectOption =
            if (selectOptions.isNotEmpty()) selectOptions.find { it.isSelected() }
                ?: selectOptions.first() else EmptyOption()
        setInternalText(selectedOption)
    }

    fun setOnOptionSelectedListener(onOptionSelectListener: OnItemSelectListener<SelectOption>?) {
        onOptionSelectedListener = onOptionSelectListener
    }

    private fun showOptions() {
        selectOptionsMenu.menu.clear()
        selectOptions.forEach { option ->
            val menuItem = selectOptionsMenu.menu.add(option.selectLabel)
            menuItem.setOnMenuItemClickListener {
                return@setOnMenuItemClickListener internalOptionsSelectListener.onItemSelected(
                    option
                )
            }
        }
        selectOptionsMenu.show()
    }

    private fun setInternalText(option: SelectOption) {
        text = option.selectLabel
    }

    private val internalOptionsSelectListener = OnItemSelectListener<SelectOption> { option ->
        setInternalText(option)
        onOptionSelectedListener?.onItemSelected(option) == true
    }

    private fun EmptyOption() =
        SelectOption("", context.getString(R.string.label_nothing_selected), true)
}