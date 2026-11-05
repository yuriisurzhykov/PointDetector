package com.yuriisurzhykov.pointdetector.uicomponents.organisms.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A small caption above a [content] block, e.g. "Working hours". */
@Immutable
class PdTitledSection(
    private val title: PdTextResolver,
    private val content: PdComponent,
) : PdBaseComponent(), PdSection {

    @Composable
    override fun Render(modifier: Modifier) {
        Column(modifier, verticalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.small)) {
            PdBasicText(title, PdTextRole.LabelSmall.style(), PdTheme.colors.content.secondary)
            content.Content(Modifier)
        }
    }
}
