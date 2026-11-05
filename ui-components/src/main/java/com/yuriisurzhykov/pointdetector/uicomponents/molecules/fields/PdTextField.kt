package com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.fields.PdBasicField
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A labelled single-line field with a [message] under it (nothing, a hint or an error). */
@Immutable
class PdTextField(
    private val label: PdTextResolver,
    private val value: String,
    private val onValueChange: (String) -> Unit,
    private val hint: PdTextResolver,
    private val message: PdFieldMessage = PdNoFieldMessage,
) : PdBaseComponent(), PdField {

    @Composable
    override fun Render(modifier: Modifier) {
        Column(modifier, verticalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.tiny)) {
            PdBasicText(label, PdTextRole.LabelMedium.style(), PdTheme.colors.content.secondary)
            PdBasicField(value, onValueChange, hint, invalid = message.isError)
            message.Content(Modifier)
        }
    }
}
