package com.yuriisurzhykov.pointdetector.uicomponents.organisms.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdTopBar

/** A form: the fields start under the top bar, one after another. */
@Immutable
class PdFormPage(
    topBar: PdTopBar,
    body: List<PdComponent>,
    footer: List<PdComponent>,
) : PdBaseFormPage(topBar, body, footer) {

    @Composable
    override fun placement(): Arrangement.Vertical = Arrangement.spacedBy(PdTheme.dimensions.spacing.regular)
}
