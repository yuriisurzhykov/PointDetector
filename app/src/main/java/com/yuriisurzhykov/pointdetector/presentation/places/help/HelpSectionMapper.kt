package com.yuriisurzhykov.pointdetector.presentation.places.help

import com.yuriisurzhykov.foodbanks.domain.points.HelpFact
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.core.Mapper
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.help.PdHelpItem
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.help.PdHelpList
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.help.PdHelpSection
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.help.PdNoHelpSection
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import javax.inject.Inject

/**
 * The "How to get help" block of a place. A kind of fact that has no factory is skipped, and when
 * nothing is left the block is [PdNoHelpSection]: the app never invents an answer.
 */
interface HelpSectionMapper : Mapper<Map<String, Boolean>, PdHelpSection> {

    /** One row per known fact, in the order of the data. */
    fun items(help: Map<String, Boolean>): List<PdHelpItem>

    class Base @Inject constructor(
        factories: Set<@JvmSuppressWildcards HelpFactFactory>,
    ) : HelpSectionMapper {

        private val byType = factories.associateBy { it.type }

        override fun items(help: Map<String, Boolean>): List<PdHelpItem> =
            help.mapNotNull { (type, enabled) -> byType[type]?.item(HelpFact(type, enabled)) }

        override fun map(from: Map<String, Boolean>): PdHelpSection {
            val items = items(from)
            return if (items.isEmpty()) PdNoHelpSection else PdHelpList(PdStringText(R.string.help_title), items)
        }
    }
}
