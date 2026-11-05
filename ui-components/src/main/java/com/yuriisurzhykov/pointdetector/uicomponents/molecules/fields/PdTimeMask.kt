package com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

private const val HOURS_DIGITS = 2

/** Shows four typed digits as a time: "0900" is drawn as "09:00". */
internal object PdTimeMask : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val shown = if (digits.length > HOURS_DIGITS) {
            digits.substring(0, HOURS_DIGITS) + ":" + digits.substring(HOURS_DIGITS)
        } else {
            digits
        }
        return TransformedText(AnnotatedString(shown), mapping)
    }

    private val mapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = if (offset <= HOURS_DIGITS) offset else offset + 1
        override fun transformedToOriginal(offset: Int): Int = if (offset <= HOURS_DIGITS) offset else offset - 1
    }
}
