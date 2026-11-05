package com.yuriisurzhykov.pointdetector.uicomponents.organisms.states

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.dp
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A bordered panel with the reason in the error colour and a "Retry" button. */
@Immutable
class PdLoadErrorPanel(
    private val message: PdTextResolver,
    onRetry: () -> Unit,
) : PdBaseComponent(), PdLoadError {

    private val retry = PdPrimaryButton(PdStringText(R.string.pd_retry), onClick = onRetry)

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val shape = PdTheme.shapes.medium
        val error = PdTheme.colors.status.error.main
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .border(PdTheme.dimensions.stroke.thin, PdTheme.colors.divider, shape)
                .padding(spacing.regular),
            verticalArrangement = Arrangement.spacedBy(spacing.regular),
        ) {
            Row(
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
                verticalAlignment = Alignment.Top,
            ) {
                PdTintedImage(PdIcons.WifiOff, error, PdIconSize.Regular.dp())
                PdBasicText(message, PdTextRole.BodyMedium.style(), error, Modifier.weight(1f))
            }
            retry.Content(Modifier.fillMaxWidth())
        }
    }
}
