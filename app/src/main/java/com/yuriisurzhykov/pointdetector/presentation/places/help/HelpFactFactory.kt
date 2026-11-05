package com.yuriisurzhykov.pointdetector.presentation.places.help

import com.yuriisurzhykov.foodbanks.domain.points.HelpFact
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.help.PdHelpItem
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tags.PdTag

/**
 * One kind of fact about how to get help. It brings its own icon and texts, so a new kind needs
 * one class and one line in the Hilt module: neither the screens nor the mappers change.
 */
interface HelpFactFactory {

    /** The key in the data, e.g. "appointment". */
    val type: String

    /** How the form of a new place asks about this kind. */
    val question: HelpQuestion

    fun item(fact: HelpFact): PdHelpItem

    /** Short marks for a card; empty when the fact does not deserve one. */
    fun tags(fact: HelpFact): List<PdTag>
}
