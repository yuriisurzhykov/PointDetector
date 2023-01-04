package com.yuriisurzhykov.pointsdetector.uicomponents.textselect

@kotlinx.serialization.Serializable
data class SelectOption(
    val selectId: String,
    val selectLabel: String,
    private var isSelected: Boolean
) {
    fun setSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }

    fun isSelected() = isSelected
}