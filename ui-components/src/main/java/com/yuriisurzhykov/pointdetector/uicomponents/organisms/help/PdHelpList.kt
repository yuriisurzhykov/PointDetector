package com.yuriisurzhykov.pointdetector.uicomponents.organisms.help

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.layout.PdHairlineDivider
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.help.PdHelpItem
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** As many rows as there are known facts. An empty list is [PdNoHelpSection], never this class. */
@Immutable
class PdHelpList(
    private val title: PdTextResolver,
    private val items: List<PdHelpItem>,
) : PdBaseComponent(), PdHelpSection {

    init {
        require(items.isNotEmpty()) { "An empty help section must be PdNoHelpSection" }
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val shape = PdTheme.shapes.medium
        Column(modifier, verticalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.small)) {
            PdBasicText(title, PdTextRole.TitleMedium.style(), PdTheme.colors.content.secondary)
            Column(
                Modifier
                    .clip(shape)
                    .border(PdTheme.dimensions.stroke.thin, PdTheme.colors.divider, shape),
            ) {
                items.forEachIndexed { index, item ->
                    if (index > 0) PdHairlineDivider.Content(Modifier)
                    item.Content(Modifier)
                }
            }
        }
    }
}
