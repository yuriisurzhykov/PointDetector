package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Stable

/** The axis can be dragged; every position goes to [consumer]. */
@Stable
class PdScrubTo(private val consumer: (Float) -> Unit) : PdScrub {
    override val enabled: Boolean = true
    override fun onScrub(position: Float) = consumer(position)
}
