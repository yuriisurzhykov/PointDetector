package com.yuriisurzhykov.pointdetector.uicomponents.workday

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import com.yuriisurzhykov.pointdetector.uicomponents.R

class HourMinuteEditText : androidx.appcompat.widget.AppCompatEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initWithAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initWithAttrs(context, attrs)
    }

    init {
        inputType = InputType.TYPE_CLASS_NUMBER
        filters = arrayOf(*filters, InputFilter.LengthFilter(2))
    }

    private fun initWithAttrs(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.HourMinuteEditText, 0, 0).run {
            val inputType = getInt(R.styleable.HourMinuteEditText_timeInputType, INPUT_TYPE_HOURS)
            initByInputType(inputType)
            recycle()
        }
    }

    private fun initByInputType(type: Int) {
        if (type == INPUT_TYPE_MINUTES) {
            filters = arrayOf(*filters, MinutesInputFilter())
        } else if (type == INPUT_TYPE_HOURS) {
            filters = arrayOf(*filters, HoursInputFilter())
        }
    }

    companion object {
        private const val INPUT_TYPE_HOURS = 1
        private const val INPUT_TYPE_MINUTES = 2
    }
}