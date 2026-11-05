package com.yuriisurzhykov.pointdetector.uicomponents.molecules.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent

/** Blocks one under another with a small gap, each as wide as the stack: rows, a message, buttons. */
@Immutable
class PdStack(private val children: List<PdComponent>) : PdBaseComponent(), PdLayout {

    @Composable
    override fun Render(modifier: Modifier) {
        Column(modifier, verticalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.small)) {
            children.forEach { it.Content(Modifier.fillMaxWidth()) }
        }
    }
}
