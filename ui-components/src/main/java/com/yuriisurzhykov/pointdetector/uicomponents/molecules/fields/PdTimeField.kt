package com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.fields.PdBasicField
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

private const val TIME_DIGITS = 4

/**
 * A field for a time of day. It keeps only the digits ("0900") and shows them as "09:00"; what
 * the digits mean as a time is decided by the mapper that reads them.
 */
@Immutable
class PdTimeField(
    private val label: PdTextResolver,
    private val digits: String,
    private val onDigitsChange: (String) -> Unit,
    private val message: PdFieldMessage = PdNoFieldMessage,
) : PdBaseComponent(), PdField {

    @Composable
    override fun Render(modifier: Modifier) {
        Column(modifier, verticalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.tiny)) {
            PdBasicText(label, PdTextRole.LabelMedium.style(), PdTheme.colors.content.secondary)
            PdBasicField(
                value = digits,
                onValueChange = { typed -> onDigitsChange(typed.filter(Char::isDigit).take(TIME_DIGITS)) },
                hint = PdLiteralText("00:00"),
                invalid = message.isError,
                transformation = PdTimeMask,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            message.Content(Modifier)
        }
    }
}
