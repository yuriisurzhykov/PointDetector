package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdNoIcon
import com.yuriisurzhykov.pointdetector.uicomponents.component.pdPressable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** The filled action of the screen. */
@Immutable
class PdPrimaryButton(
    text: PdTextResolver,
    icon: PdIcon = PdNoIcon,
    private val onClick: () -> Unit,
) : PdBaseButton(text, icon) {

    @Composable
    override fun colors(): PdButtonColors = filledButtonColors()

    @Composable
    override fun Modifier.activate(): Modifier = pdPressable(Role.Button, onClick)
}
