package com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** Today: highlighted, with the mark of the current [status] before the day name. */
@Immutable
class PdTodayWeekRow(
    private val day: PdTextResolver,
    private val hours: PdTextResolver,
    private val status: PlaceStatus,
) : PdBaseComponent(), PdWeekRow {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val primary = PdTheme.colors.primary
        val title = remember(day) { PdStringText(R.string.pd_today_format, listOf(day)) }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(primary.container)
                .padding(horizontal = spacing.regular, vertical = spacing.small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PdTintedImage(status.icon, primary.onContainer, PdTheme.dimensions.component.statusMarkSize)
                PdBasicText(title, PdTextRole.TitleSmall.style(), primary.onContainer)
            }
            PdBasicText(hours, PdTextRole.TitleSmall.style(), primary.onContainer)
        }
    }
}
