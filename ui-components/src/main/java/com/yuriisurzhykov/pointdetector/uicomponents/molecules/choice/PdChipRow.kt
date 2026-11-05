package com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent

/** Chips of equal width in one row, e.g. the seven days of the week. */
@Immutable
class PdChipRow(private val chips: List<PdChip>) : PdBaseComponent(), PdChipGroup {

    @Composable
    override fun Render(modifier: Modifier) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.small),
        ) {
            chips.forEach { it.Content(Modifier.weight(1f)) }
        }
    }
}
