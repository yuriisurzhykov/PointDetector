package com.yuriisurzhykov.pointsdetector.uicomponents

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener

class WorkDayHoursSelector : ConstraintLayout {

    private var weekDay: WeekDay? = null

    private val workDayView: TextView
    private val fromTimeInput: TextView
    private val toTimeInput: TextView
    var dayToggleChangeListener: ((WeekDay?, Boolean) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.view_work_day_hours_selector, this, true)
        workDayView = findViewById(R.id.work_day_view)
        workDayView.setOnClickListener {
            it.isSelected = !it.isSelected
            dayToggleChangeListener?.invoke(weekDay, it.isSelected)
            updateInputs()
        }
        fromTimeInput = findViewById<TextView?>(R.id.from_time_input).apply {
            addTextChangedListener { updateWeekDay() }
        }
        toTimeInput = findViewById<TextView?>(R.id.to_time_input).apply {
            addTextChangedListener { updateWeekDay() }
        }
        updateInputs()
    }

    override fun isSelected(): Boolean {
        return workDayView.isSelected
    }

    fun setWeekDay(weekDay: WeekDay) {
        this.weekDay = weekDay
        if (workDayView is WorkDayView) {
            workDayView.setWeekDay(weekDay)
        } else {
            workDayView.text = weekDay.dayName
        }
    }

    fun getWeekDay(): WeekDay = weekDay!!

    private fun updateInputs() {
        fromTimeInput.isEnabled = isSelected
        toTimeInput.isEnabled = isSelected
    }

    private fun updateWeekDay() {
        val hoursFrom = fromTimeInput.text.toString()
        val hoursTo = toTimeInput.text.toString()
        weekDay = WeekDay(weekDay?.dayName.toString(), weekDay?.dayValue ?: 0, hoursFrom, hoursTo)
    }

}