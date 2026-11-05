package com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.layout.PdHairlineDivider
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent

/** The main action of a sheet under a hairline; it stays in place while the body scrolls. */
@Immutable
class PdSheetFooter(private val action: PdComponent) : PdBaseComponent(), PdSheetContent {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        Column(modifier.fillMaxWidth()) {
            PdHairlineDivider.Content(Modifier)
            action.Content(Modifier.fillMaxWidth().padding(spacing.regular))
        }
    }
}
