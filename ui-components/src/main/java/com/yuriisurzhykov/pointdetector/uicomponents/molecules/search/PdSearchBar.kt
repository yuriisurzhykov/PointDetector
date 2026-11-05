package com.yuriisurzhykov.pointdetector.uicomponents.molecules.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.dp
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/**
 * The search entry of the main screen: a magnifier, a hint and a [trailing] slot, usually the
 * filters button. A tap anywhere on the bar opens the search.
 */
@Immutable
class PdSearchBar(
    private val placeholder: PdTextResolver,
    private val onClick: () -> Unit,
    private val trailing: PdComponent = PdNoComponent,
) : PdBaseComponent(), PdSearch {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val colors = PdTheme.colors
        val shape = PdTheme.shapes.medium
        Row(
            modifier = modifier
                .fillMaxWidth()
                .shadow(PdTheme.dimensions.elevation.small, shape)
                .clip(shape)
                .background(colors.surface.primary)
                .border(PdTheme.dimensions.stroke.thin, colors.divider, shape)
                .clickable(role = Role.Button, onClick = onClick)
                .heightIn(min = PdTheme.dimensions.component.touchTarget)
                .padding(start = spacing.regular),
            horizontalArrangement = Arrangement.spacedBy(spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PdTintedImage(PdIcons.Search, colors.content.secondary, PdIconSize.Regular.dp())
            PdBasicText(
                text = placeholder,
                style = PdTextRole.BodyLarge.style(),
                color = colors.content.secondary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            trailing.Content(Modifier)
        }
    }
}
