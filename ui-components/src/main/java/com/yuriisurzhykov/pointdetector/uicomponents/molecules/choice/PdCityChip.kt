package com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.dp
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** The selected city as a pill with a pin; a tap opens the choice of the city. */
@Immutable
class PdCityChip(
    private val label: PdTextResolver,
    private val description: PdTextResolver,
    private val onClick: () -> Unit,
) : PdBaseComponent(), PdChip {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val colors = PdTheme.colors
        val shape = PdTheme.shapes.full
        val text = description.resolve().toString()
        Row(
            modifier = modifier
                .shadow(PdTheme.dimensions.elevation.small, shape)
                .clip(shape)
                .background(colors.surface.primary)
                .border(PdTheme.dimensions.stroke.thin, colors.divider, shape)
                .clickable(role = Role.Button, onClick = onClick)
                .semantics { contentDescription = text }
                .heightIn(min = PdTheme.dimensions.component.chipMinHeight)
                .widthIn(max = PdTheme.dimensions.component.cityChipMaxWidth)
                .padding(horizontal = spacing.regular),
            horizontalArrangement = Arrangement.spacedBy(spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PdTintedImage(PdIcons.Pin, colors.primary.main, PdIconSize.Small.dp())
            PdBasicText(label, PdTextRole.LabelLarge.style(), colors.content.primary, maxLines = 1)
        }
    }
}
