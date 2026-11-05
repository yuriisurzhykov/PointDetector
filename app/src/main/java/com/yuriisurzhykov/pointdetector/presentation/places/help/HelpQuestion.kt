package com.yuriisurzhykov.pointdetector.presentation.places.help

import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/**
 * How a kind of fact is asked about in the form of a new place: the icon, the question and the
 * two answers. [order] puts the questions in a steady order whatever the order of the set is.
 */
class HelpQuestion(
    val order: Int,
    val icon: PdDrawableResolver,
    val label: PdTextResolver,
    val yes: PdTextResolver,
    val no: PdTextResolver,
)
