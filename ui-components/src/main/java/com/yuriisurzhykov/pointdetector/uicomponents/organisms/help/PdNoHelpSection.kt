package com.yuriisurzhykov.pointdetector.uicomponents.organisms.help

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent

/**
 * No facts known: nothing is drawn, not even a title or an empty gap. The app never invents
 * "no appointment needed" for a place whose data does not say so.
 */
@Immutable
object PdNoHelpSection : PdBaseComponent(), PdHelpSection {

    @Composable
    override fun Render(modifier: Modifier) = Unit
}
