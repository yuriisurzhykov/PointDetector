package com.yuriisurzhykov.pointdetector.uicomponents.organisms.hours

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdHoursTimeline
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.status.PdStatusLine

/** A bordered panel: the status line on top, the timeline of the day below it. */
@Immutable
class PdStatusHoursPanel(
    private val status: PdStatusLine,
    private val timeline: PdHoursTimeline,
) : PdBaseComponent(), PdHoursPanel {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val shape = PdTheme.shapes.medium
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .border(PdTheme.dimensions.stroke.thin, PdTheme.colors.divider, shape)
                .padding(spacing.regular),
            verticalArrangement = Arrangement.spacedBy(spacing.regular),
        ) {
            status.Content(Modifier)
            timeline.Content(Modifier)
        }
    }
}
