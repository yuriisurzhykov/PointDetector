package com.yuriisurzhykov.pointdetector.uicomponents.organisms.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdTopBar

/** A result: what happened sits in the middle of the screen, the next actions are at the bottom. */
@Immutable
class PdCenteredFormPage(
    topBar: PdTopBar,
    body: List<PdComponent>,
    footer: List<PdComponent>,
) : PdBaseFormPage(topBar, body, footer) {

    @Composable
    override fun placement(): Arrangement.Vertical =
        Arrangement.spacedBy(PdTheme.dimensions.spacing.regular, Alignment.CenterVertically)
}
