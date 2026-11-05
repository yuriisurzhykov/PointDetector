package com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.fields.PdBasicField
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdFlatIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone

/** A field for search: a magnifier before the text and a clear button while there is text. */
@Immutable
class PdSearchField(
    private val value: String,
    private val onValueChange: (String) -> Unit,
    private val hint: PdTextResolver,
    private val clearDescription: PdTextResolver,
    private val onSearch: () -> Unit,
) : PdBaseComponent(), PdField {

    @Composable
    override fun Render(modifier: Modifier) {
        PdBasicField(
            value = value,
            onValueChange = onValueChange,
            hint = hint,
            modifier = modifier,
            leading = PdDecorativeIcon(PdIcons.Search, PdIconSize.Regular, PdContentTone.Secondary),
            trailing = if (value.isEmpty()) PdNoComponent else PdFlatIconButton(PdIcons.Close, clearDescription) { onValueChange("") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        )
    }
}
