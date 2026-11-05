package com.yuriisurzhykov.pointdetector.uicomponents.molecules.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.dp
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A note in the warning colours, e.g. "No connection". */
@Immutable
open class PdWarningNotice(
    private val icon: PdDrawableResolver,
    private val text: PdTextResolver,
) : PdBaseComponent(), PdNotice {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val warning = PdTheme.colors.status.warning
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(PdTheme.shapes.medium)
                .background(warning.container)
                .padding(horizontal = spacing.regular, vertical = spacing.small),
            horizontalArrangement = Arrangement.spacedBy(spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PdTintedImage(icon, warning.onContainer, PdIconSize.Regular.dp())
            PdBasicText(text, PdTextRole.BodyMedium.style(), warning.onContainer, Modifier.weight(1f))
        }
    }
}
