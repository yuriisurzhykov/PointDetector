/*
 * Copyright (C) 2013 Paolo Conte
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuriisurzhykov.pointdetector.uicomponents.workday

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import java.util.*

/**
 * A custom EditText (actually derived from TextView) to input time in 24h format.
 *
 * Features:
 * - It always shows the currently set time, so it's never empty.
 * - Both virtual and physical keyboards can be used.
 * - The current digit is highlighted; when a number on the keyboard is pressed, the digit is replaced.
 * - Back key moves the cursor backward.
 * - Space key moves the cursor forward.
 *
 */
class TimeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyle) {
    private val digits = IntArray(4)
    private var currentPosition = POSITION_NONE
    private var mImeOptions = 0
    /**
     * @return the current hour (from 0 to 23)
     */
    /**
     * Set the current hour
     * @param hour hour (from 0 to 23)
     */
    var hour: Int = 9
        get() = digits[0] * 10 + digits[1]
        set(value) {
            field = value % 24
            digits[0] = value / 10
            digits[1] = value % 10
            updateText()
        }
    /**
     * @return the current minute
     */
    /**
     * Set the current minute
     * @param min minutes (from 0 to 59)
     */
    var minutes: Int = 0
        get() = digits[2] * 10 + digits[3]
        set(value) {
            field = value % 60
            digits[2] = value / 10
            digits[3] = value % 10
            updateText()
        }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        // hide cursor if not focused
        currentPosition = if (focused) 0 else POSITION_NONE
        updateText()
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }

    private fun updateText() {
        val bold = if (currentPosition > 1) currentPosition + 1 else currentPosition
        val text: Spannable = SpannableString(String.format("%02d:%02d", hour, minutes))
        if (bold >= 0) {
            text.setSpan(StyleSpan(Typeface.BOLD), bold, bold + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            text.setSpan(ForegroundColorSpan(Color.BLACK), bold, bold + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        setText(text)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && isEnabled) {
            requestFocusFromTouch()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, 0)
            if (currentPosition == POSITION_NONE) {
                currentPosition = 0
                updateText()
            }
        }
        return true
    }

    private fun onKeyEvent(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null && event.action != KeyEvent.ACTION_DOWN) return false
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            // moves cursor backward
            currentPosition = if (currentPosition >= 0) (currentPosition + 3) % 4 else 3
            updateText()
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            // moves cursor forward
            currentPosition = (currentPosition + 1) % 4
            updateText()
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val v = focusSearch(FOCUS_DOWN)
            var next = v != null
            if (next) {
                next = v!!.requestFocus(FOCUS_DOWN)
            }
            if (!next) {
                hideKeyboard()
                currentPosition = POSITION_NONE
                updateText()
            }
            return true
        }
        val c = event!!.unicodeChar.toChar()
        if (c in '0'..'9') {
            currentPosition = if (currentPosition == POSITION_NONE) 0 else currentPosition
            val n = c - '0'
            var valid = false
            when (currentPosition) {
                0 -> valid = n <= 2
                1 -> valid = digits[0] < 2 || n <= 3
                2 -> valid = n < 6
                3 -> valid = true
            }
            if (valid) {
                if (currentPosition == 0 && n == 2 && digits[1] > 3) { // clip to 23 hours max
                    digits[1] = 3
                }
                digits[currentPosition] = n
                currentPosition =
                    if (currentPosition < 3) currentPosition + 1 else {
                        moveToNextFocus()
                        POSITION_NONE
                    } // if it is the last digit, hide cursor
                updateText()
            }
            return true
        }
        return false
    }

    private fun moveToNextFocus() {
        (focusSearch(FOCUS_RIGHT) as? TextView)?.requestFocus() ?: run {
            clearFocus()
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // events from physical keyboard
        return onKeyEvent(keyCode, event)
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        // manage events from the virtual keyboard
        outAttrs.actionLabel = null
        outAttrs.label = "time"
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER
        outAttrs.imeOptions = mImeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI
        if (outAttrs.imeOptions and EditorInfo.IME_MASK_ACTION == EditorInfo.IME_ACTION_UNSPECIFIED) {
            if (focusSearch(FOCUS_DOWN) != null) {
                outAttrs.imeOptions = outAttrs.imeOptions or EditorInfo.IME_ACTION_NEXT
            } else {
                outAttrs.imeOptions = outAttrs.imeOptions or EditorInfo.IME_ACTION_DONE
            }
        }
        return object : BaseInputConnection(this, false) {
            override fun performEditorAction(actionCode: Int): Boolean {
                if (actionCode == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    currentPosition = POSITION_NONE
                    updateText()
                } else if (actionCode == EditorInfo.IME_ACTION_NEXT) {
                    val v = focusSearch(FOCUS_DOWN)
                    v?.requestFocus(FOCUS_DOWN)
                }
                return true
            }

            override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
                onKeyEvent(KeyEvent.KEYCODE_DEL, null)
                return true
            }

            override fun sendKeyEvent(event: KeyEvent): Boolean {
                onKeyEvent(event.keyCode, event)
                return true
            }
        }
    }

    companion object {
        private const val POSITION_NONE = -1
    }

    init {
        isFocusableInTouchMode = true
        if (attrs != null && !isInEditMode) {
            mImeOptions =
                attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "imeOptions", 0)
        }
        updateText()
    }
}