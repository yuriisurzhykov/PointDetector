package com.yuriisurzhykov.pointdetector.uicomponents.organisms.states

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdIconIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlineButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/** The selected city has no places yet: add one or choose another city. */
@Immutable
class PdEmptyPlaces(onAdd: () -> Unit, onChooseCity: () -> Unit) : PdBaseEmptyState(
    illustration = PdIconIllustration(PdIcons.Pin, large = false),
    title = PdStringText(R.string.pd_empty_places_title),
    message = PdStringText(R.string.pd_empty_places_message),
    actions = listOf(
        PdPrimaryButton(PdStringText(R.string.pd_empty_places_add), onClick = onAdd),
        PdOutlineButton(PdStringText(R.string.pd_empty_places_city), onClick = onChooseCity),
    ),
)
