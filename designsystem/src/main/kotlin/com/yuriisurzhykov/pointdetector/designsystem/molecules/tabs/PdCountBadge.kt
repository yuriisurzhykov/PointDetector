package com.yuriisurzhykov.pointdetector.designsystem.molecules.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.style
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

@Immutable
class PdCountBadge(private val count: Int) : PdBaseComponent(), PdTabBadge {

    @Composable
    override fun Render(modifier: Modifier) {
        val colors = PdTheme.colors
        val label = remember(count) { PdLiteralText(count.toString()) }
        PdBasicText(
            text = label,
            style = PdTextRole.LabelSmall.style(),
            color = colors.content.secondary,
            textAlign = TextAlign.Center,
            modifier = modifier
                .clip(PdTheme.shapes.full)
                .background(colors.surface.container)
                .defaultMinSize(minWidth = PdTheme.dimensions.component.badgeMinWidth)
                .padding(horizontal = PdTheme.dimensions.spacing.tiny),
        )
    }
}
