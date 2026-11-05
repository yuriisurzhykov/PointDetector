package com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent

/** The blocks of a sheet one under another, with the side padding of the screen; it scrolls when it is tall. */
@Immutable
class PdSheetBody(private val blocks: List<PdComponent>) : PdBaseComponent(), PdSheetContent {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = spacing.regular, end = spacing.regular, bottom = spacing.regular),
            verticalArrangement = Arrangement.spacedBy(spacing.regular),
        ) {
            blocks.forEach { it.Content(Modifier) }
        }
    }
}
