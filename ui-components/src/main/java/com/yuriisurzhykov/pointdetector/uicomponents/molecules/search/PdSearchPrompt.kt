package com.yuriisurzhykov.pointdetector.uicomponents.molecules.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdIconIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** The empty search before anything is typed: a small picture and an invitation to start. */
@Immutable
class PdSearchPrompt(private val message: PdTextResolver) : PdBaseComponent(), PdSearch {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        Column(
            modifier = modifier.fillMaxWidth().padding(top = spacing.xLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.regular),
        ) {
            PdIconIllustration(PdIcons.Search, large = false).Content(Modifier)
            PdBasicText(message, PdTextRole.BodyMedium.style(), PdTheme.colors.content.secondary, textAlign = TextAlign.Center)
        }
    }
}
