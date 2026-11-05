package com.yuriisurzhykov.pointdetector.uicomponents.organisms.states

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdIconIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlineButton
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A search found nothing; the person may add the place by hand. */
@Immutable
class PdNoMatches(query: PdTextResolver, onAdd: () -> Unit) : PdBaseEmptyState(
    illustration = PdIconIllustration(PdIcons.Search, large = false),
    title = PdStringText(R.string.pd_empty_matches_title, listOf(query)),
    message = PdStringText(R.string.pd_empty_matches_message),
    actions = listOf(PdOutlineButton(PdStringText(R.string.pd_empty_places_add), onClick = onAdd)),
)
