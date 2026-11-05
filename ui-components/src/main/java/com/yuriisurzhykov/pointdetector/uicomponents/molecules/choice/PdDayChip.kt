package com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.pdPressable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A short toggle for one day: filled with the brand colour when it is on. */
@Immutable
class PdDayChip(
    private val label: PdTextResolver,
    private val selected: Boolean,
    private val onToggle: (Boolean) -> Unit,
) : PdBaseComponent(), PdChip {

    @Composable
    override fun Render(modifier: Modifier) {
        val colors = PdTheme.colors
        val shape = PdTheme.shapes.medium
        Box(
            modifier = modifier
                .heightIn(min = PdTheme.dimensions.component.chipMinHeight)
                .clip(shape)
                .background(if (selected) colors.primary.main else colors.surface.primary)
                .border(
                    PdTheme.dimensions.stroke.thin,
                    if (selected) colors.primary.main else colors.divider,
                    shape,
                )
                .toggleable(value = selected, role = Role.Checkbox, onValueChange = onToggle),
            contentAlignment = Alignment.Center,
        ) {
            PdBasicText(
                text = label,
                style = PdTextRole.LabelLarge.style(),
                color = if (selected) colors.primary.onMain else colors.content.secondary,
                maxLines = 1,
            )
        }
    }
}
