package com.yuriisurzhykov.pointdetector.uicomponents.molecules.snackbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A message with one action, e.g. "Undo". */
@Immutable
class PdActionSnackbar(
    message: PdTextResolver,
    private val action: PdTextResolver,
    private val onAction: () -> Unit,
) : PdBaseSnackbar(message) {

    @Composable
    override fun RowScope.Action() {
        val colors = PdTheme.colors
        PdBasicText(
            text = action,
            style = PdTextRole.LabelLarge.style(),
            color = colors.secondary.main,
            modifier = Modifier
                .heightIn(min = PdTheme.dimensions.component.touchTarget)
                .clickable(role = Role.Button, onClick = onAction)
                .padding(horizontal = PdTheme.dimensions.spacing.regular),
        )
    }
}
