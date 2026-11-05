package com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.feedback.PdSkeletonBlock
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.feedback.PdSkeletonSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.feedback.PdSkeletonWidth
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent

/** Without data: a row that is still loading. */
@Immutable
object PdSkeletonListRow : PdBaseComponent(), PdListRow {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.regular, vertical = spacing.small),
            horizontalArrangement = Arrangement.spacedBy(spacing.regular),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PdSkeletonBlock(PdSkeletonSize.Chip, PdSkeletonWidth.Full)
                .Content(Modifier.size(PdTheme.dimensions.component.rowIconBox))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                PdSkeletonBlock(PdSkeletonSize.Title, PdSkeletonWidth.Wide).Content(Modifier)
                PdSkeletonBlock(PdSkeletonSize.Line, PdSkeletonWidth.Narrow).Content(Modifier)
            }
        }
    }
}
