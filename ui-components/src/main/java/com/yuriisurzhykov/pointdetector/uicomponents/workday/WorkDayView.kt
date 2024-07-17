package com.yuriisurzhykov.pointdetector.uicomponents.workday

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.format.DateFormat.is24HourFormat
import android.util.AttributeSet
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.workday.entity.WeekDay

class WorkDayView : androidx.appcompat.widget.AppCompatTextView {

    private var rectanglePaint = Paint(paint)
    private val rectangle = RectF()
    private val paintRadius = 10 * density
    private val selectedColor = Color.parseColor("#C6F9C8")
    private val disabledColor = Color.parseColor("#ffffff")

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        rectanglePaint.color = getBackgroundColorForState(isSelected)
        rectanglePaint.strokeWidth = 1f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = measuredWidth
        val height = measuredHeight
        rectangle.set(
            0f + paddingLeft,
            0f + paddingTop,
            width.toFloat() - paddingRight,
            height.toFloat() - paddingBottom
        )
    }

    private val r = Rect()

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(rectangle, paintRadius, paintRadius, rectanglePaint)
        canvas.getClipBounds(r)
        val cHeight = r.height()
        val cWidth = r.width()
        paint.textAlign = Paint.Align.LEFT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            paint.getTextBounds(text, 0, text.length, r)
        } else {
            paint.getTextBounds(text.toString(), 0, text.length, r)
        }
        val x = cWidth / 2f - r.width() / 2f - r.left
        val y = cHeight / 2f + r.height() / 2f - r.bottom
        canvas.drawText(text.toString(), x, y, paint)
    }

    fun setWeekDay(weekDay: WeekDay) {
        text = weekDay.dayName
    }

    override fun setSelected(isSelected: Boolean) {
        super.setSelected(isSelected)
        rectanglePaint.color = getBackgroundColorForState(isSelected)
        if (isSelected && !this.isSelected) {
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText(R.string.title_working_hours_input)
                .setTimeFormat(if (is24HourFormat(context)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
                .build()
            timePicker.addOnPositiveButtonClickListener {

            }
        }
    }

    private fun getBackgroundColorForState(selected: Boolean): Int {
        return if (selected) selectedColor else disabledColor
    }
}