package com.yuriisurzhykov.pointdetector.uicomponents.molecules.tabs

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** A text tab with an underline that fades in when it is selected. */
@Immutable
class PdTextTab(
    private val label: PdTextResolver,
    private val badge: PdTabBadge,
    private val selected: Boolean,
    private val onSelect: () -> Unit,
) : PdBaseComponent(), PdTab {

    @Composable
    override fun Render(modifier: Modifier) {
        val colors = PdTheme.colors
        val dimensions = PdTheme.dimensions
        val underline by animateColorAsState(
            targetValue = if (selected) colors.primary.main else Color.Transparent,
            animationSpec = PdTheme.motion.quick(),
            label = "underline",
        )
        val textColor = if (selected) colors.content.primary else colors.content.secondary
        Row(
            modifier = modifier
                .heightIn(min = dimensions.component.tabMinHeight)
                .selectable(selected = selected, role = Role.Tab, onClick = onSelect)
                .drawBehind {
                    val thickness = dimensions.stroke.thick.toPx()
                    drawLine(underline, Offset(0f, size.height), Offset(size.width, size.height), thickness)
                }
                .padding(horizontal = dimensions.spacing.small),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PdBasicText(label, PdTextRole.LabelLarge.style(), textColor, maxLines = 1)
            badge.Content(Modifier)
        }
    }
}
