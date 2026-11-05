package com.yuriisurzhykov.pointdetector.uicomponents.model.hours

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** When a closed place opens next, as two ready texts instead of a date and a time. */
@Immutable
data class PdNextOpening(val day: PdTextResolver, val hours: PdTextResolver)
