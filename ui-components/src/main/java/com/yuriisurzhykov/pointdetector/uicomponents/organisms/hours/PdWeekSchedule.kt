package com.yuriisurzhykov.pointdetector.uicomponents.organisms.hours

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.layout.PdHairlineDivider
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.hours.PdWeekRow

/** Seven rows in a bordered list. Days off are rows too ([PdWeekRow]), so the list is never empty. */
@Immutable
class PdWeekSchedule(private val rows: List<PdWeekRow>) : PdBaseComponent(), PdWeekHours {

    init {
        require(rows.isNotEmpty()) { "A schedule needs at least one row" }
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val shape = PdTheme.shapes.medium
        Column(
            modifier
                .clip(shape)
                .border(PdTheme.dimensions.stroke.thin, PdTheme.colors.divider, shape),
        ) {
            rows.forEachIndexed { index, row ->
                if (index > 0) PdHairlineDivider.Content(Modifier)
                row.Content(Modifier)
            }
        }
    }
}
