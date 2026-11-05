package com.yuriisurzhykov.pointdetector.presentation.places

import com.yuriisurzhykov.foodbanks.domain.points.HelpFact
import com.yuriisurzhykov.pointdetector.presentation.places.help.HelpFactFactory
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tags.PdTag
import javax.inject.Inject

/** The marks on a card, asked from the factory of every known kind of fact. Unknown kinds are skipped. */
class PlaceTagsMapper @Inject constructor(
    factories: Set<@JvmSuppressWildcards HelpFactFactory>,
) {
    private val byType = factories.associateBy { it.type }

    fun tags(help: Map<String, Boolean>): List<PdTag> =
        help.entries
            .flatMap { (type, enabled) -> byType[type]?.tags(HelpFact(type, enabled)).orEmpty() }
            .take(MAX_TAGS)

    private companion object {
        const val MAX_TAGS = 3
    }
}
