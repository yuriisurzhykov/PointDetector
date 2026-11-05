package com.yuriisurzhykov.pointdetector.designsystem.organisms.tabs

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.atoms.layout.PdHairlineDivider
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.molecules.tabs.PdTab

/** A scrollable row of tabs over a hairline. */
@Immutable
class PdTextTabs(private val tabs: List<PdTab>) : PdBaseComponent(), PdFilterTabs {

    @Composable
    override fun Render(modifier: Modifier) {
        Column(modifier) {
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                tabs.forEach { it.Content(Modifier) }
            }
            PdHairlineDivider.Content(Modifier)
        }
    }
}
