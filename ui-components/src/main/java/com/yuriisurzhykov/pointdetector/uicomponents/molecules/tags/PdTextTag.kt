package com.yuriisurzhykov.pointdetector.uicomponents.molecules.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** A small neutral label: "No appointment", "ID required". */
@Immutable
class PdTextTag(private val text: PdTextResolver) : PdBaseComponent(), PdTag {

    @Composable
    override fun Render(modifier: Modifier) {
        val component = PdTheme.dimensions.component
        val neutral = PdTheme.colors.status.neutral
        PdBasicText(
            text = text,
            style = PdTextRole.LabelSmall.style(),
            color = neutral.onContainer,
            modifier = modifier
                .clip(PdTheme.shapes.small)
                .background(neutral.container)
                .padding(horizontal = component.tagHorizontalPadding, vertical = component.tagVerticalPadding),
        )
    }
}
