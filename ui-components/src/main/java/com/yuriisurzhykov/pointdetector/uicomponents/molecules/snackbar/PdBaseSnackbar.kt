package com.yuriisurzhykov.pointdetector.uicomponents.molecules.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** The dark strip with a message; a subclass may add an action at the end. */
@Immutable
abstract class PdBaseSnackbar(private val message: PdTextResolver) : PdBaseComponent(), PdSnackbar {

    @Composable
    protected open fun RowScope.Action() = Unit

    @Composable
    final override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val colors = PdTheme.colors
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(PdTheme.shapes.medium)
                .background(colors.content.primary)
                .padding(start = spacing.regular, top = spacing.tiny, end = spacing.tiny, bottom = spacing.tiny)
                .semantics { liveRegion = LiveRegionMode.Polite },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PdBasicText(message, PdTextRole.BodyMedium.style(), colors.surface.primary, Modifier.weight(1f))
            Action()
        }
    }
}
