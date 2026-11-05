package com.yuriisurzhykov.pointdetector.uicomponents.molecules.summary

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A bordered recap of what was done: a title and a few quiet lines under it. */
@Immutable
class PdSummaryCard(
    private val title: PdTextResolver,
    private val lines: List<PdTextResolver>,
) : PdBaseComponent(), PdSummary {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val shape = PdTheme.shapes.medium
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .border(PdTheme.dimensions.stroke.thin, PdTheme.colors.divider, shape)
                .padding(spacing.regular),
            verticalArrangement = Arrangement.spacedBy(spacing.xxSmall),
        ) {
            PdBasicText(title, PdTextRole.TitleMedium.style(), PdTheme.colors.content.primary)
            lines.forEach { PdBasicText(it, PdTextRole.BodySmall.style(), PdTheme.colors.content.secondary) }
        }
    }
}
