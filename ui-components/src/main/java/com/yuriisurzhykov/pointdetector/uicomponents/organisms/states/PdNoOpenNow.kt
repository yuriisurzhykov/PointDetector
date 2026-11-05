package com.yuriisurzhykov.pointdetector.uicomponents.organisms.states

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdIconIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/** Without actions: no place is open at this moment; the other tabs may still have some. */
@Immutable
object PdNoOpenNow : PdBaseEmptyState(
    illustration = PdIconIllustration(PdIcons.Clock, large = false),
    title = PdStringText(R.string.pd_empty_open_title),
    message = PdStringText(R.string.pd_empty_open_message),
    actions = emptyList(),
)
