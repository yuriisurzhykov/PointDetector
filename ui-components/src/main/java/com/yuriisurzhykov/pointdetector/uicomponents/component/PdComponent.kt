package com.yuriisurzhykov.pointdetector.uicomponents.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

/**
 * Contract of every block of the interface: it knows how to draw itself.
 * Consumers (screens, other components) depend on this interface only.
 */
@Stable
interface PdComponent {

    @Composable
    fun Content(modifier: Modifier)
}
