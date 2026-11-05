package com.yuriisurzhykov.pointdetector.presentation.filter

/** One thing to choose: the id that is stored in the config and the text shown for it. */
data class FilterOption(val id: String, val label: String)

/**
 * The filters sheet before "Apply": the options, and what is chosen so far. The choice becomes
 * the config only when the person applies it.
 */
data class FiltersState(
    val sorts: List<FilterOption> = emptyList(),
    val units: List<FilterOption> = emptyList(),
    val sortId: String = FilterDefaults.SORT,
    val unitId: String = "",
    val applied: Boolean = false,
) {
    /** The options are read; before that there is nothing to show and a segmented control cannot be built. */
    val isReady: Boolean get() = sorts.isNotEmpty() && units.size >= 2
}
