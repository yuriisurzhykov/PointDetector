package com.yuriisurzhykov.pointdetector.uicomponents.organisms.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.layout.PdHairlineDivider
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdStepIndicator
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdTopBar

/** A top bar with a step indicator under it and a hairline at the bottom. */
@Immutable
class PdStepTopBar(
    private val topBar: PdTopBar,
    private val steps: PdStepIndicator,
) : PdBaseComponent(), PdTopBar {

    @Composable
    override fun Render(modifier: Modifier) {
        Column(modifier.background(PdTheme.colors.surface.primary)) {
            topBar.Content(Modifier)
            steps.Content(Modifier)
            PdHairlineDivider.Content(Modifier)
        }
    }
}
