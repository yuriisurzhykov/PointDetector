package com.yuriisurzhykov.pointdetector.uicomponents.organisms.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdPage
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/**
 * A whole screen that says one thing: a picture, a title and a few lines in the middle, and what
 * can be done (a hint, the main button, a quiet one) pinned to the bottom. Permission requests
 * and other full-screen messages use it.
 */
@Immutable
class PdMessagePage(
    private val illustration: PdIllustration,
    private val title: PdTextResolver,
    private val message: PdTextResolver,
    private val footer: List<PdComponent>,
) : PdBaseComponent(), PdPage {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val content = PdTheme.colors.content
        Column(modifier.fillMaxSize().background(PdTheme.colors.surface.primary)) {
            Box(
                Modifier.weight(1f).fillMaxWidth().statusBarsPadding().padding(horizontal = spacing.xLarge),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing.regular),
                ) {
                    illustration.Content(Modifier)
                    PdBasicText(title, PdTextRole.HeadingMedium.style(), content.primary, textAlign = TextAlign.Center)
                    PdBasicText(message, PdTextRole.BodyMedium.style(), content.secondary, textAlign = TextAlign.Center)
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(spacing.regular),
                verticalArrangement = Arrangement.spacedBy(spacing.small),
            ) {
                footer.forEach { it.Content(Modifier.fillMaxWidth()) }
            }
        }
    }
}
