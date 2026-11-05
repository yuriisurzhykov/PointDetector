package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdNoIcon
import com.yuriisurzhykov.pointdetector.uicomponents.component.pdPressable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** The action that cannot be taken back, e.g. "Delete": filled with the error colour. */
@Immutable
class PdDangerButton(
    text: PdTextResolver,
    icon: PdIcon = PdNoIcon,
    private val onClick: () -> Unit,
) : PdBaseButton(text, icon) {

    @Composable
    override fun colors(): PdButtonColors {
        val error = PdTheme.colors.status.error
        return PdButtonColors(container = error.main, content = error.onMain, border = Color.Transparent)
    }

    @Composable
    override fun Modifier.activate(): Modifier = pdPressable(Role.Button, onClick)
}
