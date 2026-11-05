package com.yuriisurzhykov.pointdetector.uicomponents.organisms.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.tabs.PdFilterTabs
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** "5 places nearby", the sort order on the right and the filter tabs below. */
@Immutable
class PdPlacesHeader(
    private val title: PdTextResolver,
    private val order: PdTextResolver,
    private val tabs: PdFilterTabs,
) : PdBaseComponent(), PdListHeader {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val content = PdTheme.colors.content
        Column(modifier, verticalArrangement = Arrangement.spacedBy(spacing.small)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.regular),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                PdBasicText(title, PdTextRole.HeadingSmall.style(), content.primary, Modifier.weight(1f), maxLines = 1)
                PdBasicText(order, PdTextRole.BodySmall.style(), content.secondary, maxLines = 1)
            }
            tabs.Content(Modifier.padding(horizontal = spacing.small))
        }
    }
}
