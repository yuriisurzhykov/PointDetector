package com.yuriisurzhykov.pointdetector.uicomponents.workday

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.*
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay
import java.text.SimpleDateFormat
import java.util.*

class WorkingDaysSelector : LinearLayout {

    private var mSectionDrawable: Drawable? = null
    private var mSectionRadius: Int = 3
    private val weekDaysList by lazy { getWeekDays() }
    private var selectedDaysChangedListener: ((List<WeekDay>) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) {
        orientation = VERTICAL
        context.obtainStyledAttributes(attrs, R.styleable.WorkDaySelector, defStyleAttr, 0).run {
            mSectionDrawable = getDrawable(R.styleable.WorkDaySelector_workDaySectionBackground)
            mSectionRadius = getDimensionPixelSize(
                R.styleable.WorkDaySelector_workDayRadius,
                (3 * density).toInt()
            )
            recycle()
        }
        for (weekDay in weekDaysList) {
            val view = WorkDayHoursSelector(context, attrs, defStyleAttr)
            view.setWeekDay(weekDay)
            view.dayToggleChangeListener = { _, _ ->
                selectedDaysChangedListener?.invoke(getSelectedDays())
            }
            addView(view)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val newMeasuredHeight = measuredHeight / 7 - child.marginTop - child.marginBottom
            val heightParam =
                if (isHorizontalOrientation()) ViewGroup.LayoutParams.WRAP_CONTENT else newMeasuredHeight
            child.updateLayoutParams {
                height = heightParam
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            child.minimumHeight = newMeasuredHeight
        }
    }

    private fun isHorizontalOrientation(): Boolean {
        return measuredWidth > measuredHeight
    }

    fun setSelectedDays(days: List<WeekDay>) {
        val localListener = selectedDaysChangedListener
        selectedDaysChangedListener = null
        days.forEach { day ->
            val index = weekDaysList.indexOf(day)
            if (index != -1) {
                getChildAt(index)?.isSelected = true
            }
        }
        selectedDaysChangedListener = localListener
    }

    fun setOnSelectedDaysChangedListener(block: (List<WeekDay>) -> Unit) {
        selectedDaysChangedListener = block
    }

    fun getSelectedDays(): List<WeekDay> {
        val list = mutableListOf<WeekDay>()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.isSelected) {
                list.add((child as WorkDayHoursSelector).getWeekDay())
            }
        }
        return list
    }

    private fun getWeekDays(): List<WeekDay> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val daysList = mutableListOf<WeekDay>()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK))
        for (i in 0..6) {
            val weekDay = WeekDay(dateFormat.format(calendar.time), i + 1, "", "")
            daysList.add(weekDay)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return daysList
    }

}