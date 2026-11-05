package com.yuriisurzhykov.pointdetector.designsystem.molecules.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdIcon
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdNoIcon
import com.yuriisurzhykov.pointdetector.designsystem.component.pdPressable
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdTextResolver

/** A secondary action that sits next to a filled one. */
@Immutable
class PdOutlineButton(
    text: PdTextResolver,
    icon: PdIcon = PdNoIcon,
    private val onClick: () -> Unit,
) : PdBaseButton(text, icon) {

    @Composable
    override fun colors(): PdButtonColors = outlinedButtonColors()

    @Composable
    override fun Modifier.activate(): Modifier = pdPressable(Role.Button, onClick)
}
