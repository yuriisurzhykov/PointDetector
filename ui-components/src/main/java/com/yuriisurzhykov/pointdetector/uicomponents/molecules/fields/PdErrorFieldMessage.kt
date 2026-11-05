package com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
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

/** An error under a field: an icon and a red text, announced by TalkBack when it appears. */
@Immutable
class PdErrorFieldMessage(private val text: PdTextResolver) : PdBaseComponent(), PdFieldMessage {
    override val isError: Boolean = true

    @Composable
    override fun Render(modifier: Modifier) {
        val error = PdTheme.colors.status.error.main
        Row(
            modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite },
            horizontalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.tiny),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PdTintedImage(PdIcons.Error, error, PdIconSize.Small.dp())
            PdBasicText(text, PdTextRole.BodySmall.style(), error)
        }
    }
}
