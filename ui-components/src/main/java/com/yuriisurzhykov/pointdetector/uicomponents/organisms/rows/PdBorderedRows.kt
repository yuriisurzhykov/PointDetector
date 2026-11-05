package com.yuriisurzhykov.pointdetector.uicomponents.organisms.rows

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.layout.PdHairlineDivider
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdListRow

/** Rows separated by hairlines inside one rounded border. Exclusive options are read as a group. */
@Immutable
class PdBorderedRows(private val rows: List<PdListRow>) : PdBaseComponent(), PdRows {

    init {
        require(rows.isNotEmpty()) { "A group needs at least one row" }
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val shape = PdTheme.shapes.medium
        Column(
            modifier
                .clip(shape)
                .background(PdTheme.colors.surface.primary)
                .border(PdTheme.dimensions.stroke.thin, PdTheme.colors.divider, shape)
                .selectableGroup(),
        ) {
            rows.forEachIndexed { index, row ->
                if (index > 0) PdHairlineDivider.Content(Modifier)
                row.Content(Modifier)
            }
        }
    }
}
