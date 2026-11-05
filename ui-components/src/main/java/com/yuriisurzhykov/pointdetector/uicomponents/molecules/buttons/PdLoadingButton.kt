package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.feedback.PdArcSpinner
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdNoIcon
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/**
 * A button that is working. Having no click handler at all is the point: the state is a
 * type, so a loading button cannot be tapped by construction.
 */
@Immutable
class PdLoadingButton(text: PdTextResolver) : PdBaseButton(text, PdNoIcon) {

    override val leading: PdComponent = PdArcSpinner()

    @Composable
    override fun colors(): PdButtonColors = filledButtonColors()

    @Composable
    override fun Modifier.activate(): Modifier = this
}
