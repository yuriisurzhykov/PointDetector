package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.pdPressable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A quiet action with no container, in the brand colour, e.g. "Reset" in the header of a sheet. */
@Immutable
class PdTextButton(
    private val text: PdTextResolver,
    private val onClick: () -> Unit,
) : PdBaseComponent(), PdButton {

    @Composable
    override fun Render(modifier: Modifier) {
        Box(
            modifier = modifier
                .pdPressable(Role.Button, onClick)
                .defaultMinSize(minHeight = PdTheme.dimensions.component.touchTarget)
                .padding(horizontal = PdTheme.dimensions.spacing.small),
            contentAlignment = Alignment.Center,
        ) {
            PdBasicText(text, PdTextRole.LabelLarge.style(), PdTheme.colors.primary.main, maxLines = 1)
        }
    }
}
