package com.yuriisurzhykov.foodbanks.domain.points

/**
 * The known facts of a point. A fact the data does not have is simply not in the list: the app
 * never turns an unknown answer into "no".
 */
data class PointHelp(val facts: List<HelpFact>) {

    companion object {
        val None = PointHelp(emptyList())
    }
}
