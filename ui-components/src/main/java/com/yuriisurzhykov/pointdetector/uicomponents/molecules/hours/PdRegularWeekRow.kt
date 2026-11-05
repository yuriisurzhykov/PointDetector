package com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A working day that is not today. */
@Immutable
class PdRegularWeekRow(
    private val day: PdTextResolver,
    private val hours: PdTextResolver,
) : PdBaseComponent(), PdWeekRow {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val content = PdTheme.colors.content
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.regular, vertical = spacing.small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PdBasicText(day, PdTextRole.BodyMedium.style(), content.primary)
            PdBasicText(hours, PdTextRole.BodyMedium.style(), content.primary)
        }
    }
}
