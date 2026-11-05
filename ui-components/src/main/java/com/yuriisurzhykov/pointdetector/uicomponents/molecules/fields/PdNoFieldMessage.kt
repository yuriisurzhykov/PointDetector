package com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent

/** Without data: nothing under the field. */
@Immutable
object PdNoFieldMessage : PdBaseComponent(), PdFieldMessage {
    override val isError: Boolean = false

    @Composable
    override fun Render(modifier: Modifier) = Unit
}
