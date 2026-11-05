package com.yuriisurzhykov.pointdetector.uicomponents.organisms.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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

/** What the middle of a result screen says: a picture, what happened and a recap under it. */
@Immutable
class PdResultPanel(
    private val illustration: PdIllustration,
    private val title: PdTextResolver,
    private val summary: PdComponent,
) : PdBaseComponent(), PdMessage {

    @Composable
    override fun Render(modifier: Modifier) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.regular),
        ) {
            illustration.Content(Modifier)
            PdBasicText(title, PdTextRole.HeadingMedium.style(), PdTheme.colors.content.primary, textAlign = TextAlign.Center)
            summary.Content(Modifier)
        }
    }
}
