package com.yuriisurzhykov.pointdetector.designsystem.molecules.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent

/** A tab without a counter. Draws nothing. */
@Immutable
object PdNoBadge : PdBaseComponent(), PdTabBadge {

    @Composable
    override fun Render(modifier: Modifier) = Unit
}
