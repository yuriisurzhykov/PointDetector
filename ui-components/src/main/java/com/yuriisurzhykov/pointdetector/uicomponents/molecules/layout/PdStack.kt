package com.yuriisurzhykov.pointdetector.uicomponents.molecules.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent

/** Blocks one under another with a small gap, e.g. a group of rows and the message under it. */
@Immutable
class PdStack(private val children: List<PdComponent>) : PdBaseComponent(), PdLayout {

    @Composable
    override fun Render(modifier: Modifier) {
        Column(modifier, verticalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.small)) {
            children.forEach { it.Content(Modifier) }
        }
    }
}
