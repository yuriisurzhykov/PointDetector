package com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A quiet line of help under a field. */
@Immutable
class PdHintFieldMessage(private val text: PdTextResolver) : PdBaseComponent(), PdFieldMessage {
    override val isError: Boolean = false

    @Composable
    override fun Render(modifier: Modifier) {
        PdBasicText(text, PdTextRole.BodySmall.style(), PdTheme.colors.content.secondary, modifier)
    }
}
