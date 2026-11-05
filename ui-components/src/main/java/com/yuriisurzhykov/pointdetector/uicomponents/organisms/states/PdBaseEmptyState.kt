package com.yuriisurzhykov.pointdetector.uicomponents.organisms.states

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
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/**
 * The shared layout of an empty state: a picture, a title, a message and the actions below.
 * Each state is its own class, because what is said and what can be done differ.
 */
@Immutable
abstract class PdBaseEmptyState(
    private val illustration: PdIllustration,
    private val title: PdTextResolver,
    private val message: PdTextResolver,
    private val actions: List<PdComponent>,
) : PdBaseComponent(), PdEmptyState {

    @Composable
    final override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val content = PdTheme.colors.content
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.xLarge, vertical = spacing.xLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.regular),
        ) {
            illustration.Content(Modifier)
            PdBasicText(title, PdTextRole.HeadingSmall.style(), content.primary, textAlign = TextAlign.Center)
            PdBasicText(message, PdTextRole.BodyMedium.style(), content.secondary, textAlign = TextAlign.Center)
            actions.forEach { it.Content(Modifier.fillMaxWidth()) }
        }
    }
}
