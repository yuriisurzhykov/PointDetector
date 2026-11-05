package com.yuriisurzhykov.pointdetector.uicomponents.model

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** One option of a group of exclusive options: its text, whether it is chosen and what choosing does. */
@Immutable
class PdChoice(
    val label: PdTextResolver,
    val selected: Boolean,
    val onSelect: () -> Unit,
)
