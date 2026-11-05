package com.yuriisurzhykov.pointdetector.presentation.places.help

import com.yuriisurzhykov.foodbanks.domain.points.HelpFact
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.help.PdFactItem
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.help.PdHelpItem
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tags.PdTag
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.tags.PdTextTag
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import javax.inject.Inject

class DocumentsFactFactory @Inject constructor() : HelpFactFactory {

    override val type: String = "documents"

    override val question = HelpQuestion(
        order = 1,
        icon = PdIcons.Id,
        label = PdStringText(R.string.help_documents),
        yes = PdStringText(R.string.help_documents_required),
        no = PdStringText(R.string.help_documents_not_required),
    )

    override fun item(fact: HelpFact): PdHelpItem = PdFactItem(
        icon = PdIcons.Id,
        label = PdStringText(R.string.help_documents),
        value = PdStringText(
            if (fact.enabled) R.string.help_documents_required else R.string.help_documents_not_required,
        ),
    )

    /** Only "documents needed" is worth a mark on a card; "not needed" is the usual case. */
    override fun tags(fact: HelpFact): List<PdTag> =
        if (fact.enabled) listOf(PdTextTag(PdStringText(R.string.tag_documents))) else emptyList()
}
