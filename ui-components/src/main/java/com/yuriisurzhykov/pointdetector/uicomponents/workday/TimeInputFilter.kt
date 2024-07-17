package com.yuriisurzhykov.pointdetector.uicomponents.workday

import android.text.InputFilter
import android.text.Spanned

class HoursInputFilter : AbstractTimeInputFilter(1, 12)

class MinutesInputFilter : AbstractTimeInputFilter(0, 59)

abstract class AbstractTimeInputFilter(private val minIntValue: Int, private val maxIntValue: Int) :
    InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toInt()
            if (isInRange(minIntValue, maxIntValue, input)) return null
        } catch (nfe: NumberFormatException) {
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}