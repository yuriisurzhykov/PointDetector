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

class AppointmentFactFactory @Inject constructor() : HelpFactFactory {

    override val type: String = "appointment"

    override fun item(fact: HelpFact): PdHelpItem = PdFactItem(
        icon = PdIcons.Calendar,
        label = PdStringText(R.string.help_appointment),
        value = PdStringText(
            if (fact.enabled) R.string.help_appointment_required else R.string.help_appointment_not_required,
        ),
    )

    override fun tags(fact: HelpFact): List<PdTag> =
        listOf(PdTextTag(PdStringText(if (fact.enabled) R.string.tag_appointment else R.string.tag_walk_in)))
}
