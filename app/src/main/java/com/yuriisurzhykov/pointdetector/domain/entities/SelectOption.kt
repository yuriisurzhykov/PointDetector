package com.yuriisurzhykov.pointdetector.domain.entities

/** One option of a setting: the id that is stored, the text shown for it and whether it is the current one. */
data class SelectOption(
    val selectId: String,
    val selectLabel: String,
    val isSelected: Boolean
)
