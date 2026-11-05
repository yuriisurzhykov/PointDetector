package com.yuriisurzhykov.pointdetector.uicomponents.organisms.states

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdIconIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/** Without actions: nothing is marked as a favorite yet. */
@Immutable
object PdNoFavorites : PdBaseEmptyState(
    illustration = PdIconIllustration(PdIcons.Heart, large = false),
    title = PdStringText(R.string.pd_empty_favorites_title),
    message = PdStringText(R.string.pd_empty_favorites_message),
    actions = emptyList(),
)
