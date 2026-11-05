package com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdChoice

/** Equal-width options in a tinted track; the chosen one is lifted on a white pill. */
@Immutable
class PdChoiceSegments(private val choices: List<PdChoice>) : PdBaseComponent(), PdSegmentedControl {

    init {
        require(choices.size >= 2) { "A segmented control needs at least two options" }
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val colors = PdTheme.colors
        val shape = PdTheme.shapes.medium
        val inner = PdTheme.shapes.small
        val spacing = PdTheme.dimensions.spacing
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .background(colors.surface.container)
                .border(PdTheme.dimensions.stroke.thin, colors.divider, shape)
                .padding(spacing.tiny)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(spacing.xxSmall),
        ) {
            choices.forEach { choice ->
                val lift = if (choice.selected) Modifier.shadow(PdTheme.dimensions.elevation.small, inner) else Modifier
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = PdTheme.dimensions.component.segmentMinHeight)
                        .then(lift)
                        .clip(inner)
                        .background(if (choice.selected) colors.surface.primary else colors.surface.container)
                        .selectable(selected = choice.selected, role = Role.RadioButton, onClick = choice.onSelect),
                    contentAlignment = Alignment.Center,
                ) {
                    PdBasicText(
                        text = choice.label,
                        style = PdTextRole.LabelLarge.style(),
                        color = if (choice.selected) colors.content.primary else colors.content.secondary,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
