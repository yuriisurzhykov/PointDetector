package com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdFlatIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A title and a back button, below the status bar. */
@Immutable
class PdBackTopBar(
    private val title: PdTextResolver,
    private val backDescription: PdTextResolver,
    private val onBack: () -> Unit,
) : PdBaseComponent(), PdTopBar {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(PdTheme.colors.surface.primary)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = spacing.small, vertical = spacing.tiny),
            horizontalArrangement = Arrangement.spacedBy(spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PdFlatIconButton(PdIcons.Back, backDescription, onBack).Content(Modifier)
            PdBasicText(title, PdTextRole.TitleLarge.style(), PdTheme.colors.content.primary, maxLines = 1)
        }
    }
}
