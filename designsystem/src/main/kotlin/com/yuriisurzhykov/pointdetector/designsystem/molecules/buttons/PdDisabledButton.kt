package com.yuriisurzhykov.pointdetector.designsystem.molecules.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdNoIcon
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdTextResolver

/** A button that cannot be used right now. Like [PdLoadingButton], it has no click handler. */
@Immutable
class PdDisabledButton(text: PdTextResolver) : PdBaseButton(text, PdNoIcon) {

    override val enabled: Boolean = false

    @Composable
    override fun colors(): PdButtonColors = filledButtonColors()

    @Composable
    override fun Modifier.activate(): Modifier = semantics { disabled() }
}
