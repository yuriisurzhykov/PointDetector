package com.yuriisurzhykov.pointdetector.uicomponents.molecules.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent

/** Two blocks of equal width side by side, e.g. the "From" and "To" fields. */
@Immutable
class PdPairRow(
    private val first: PdComponent,
    private val second: PdComponent,
) : PdBaseComponent(), PdLayout {

    @Composable
    override fun Render(modifier: Modifier) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.small),
            verticalAlignment = Alignment.Top,
        ) {
            first.Content(Modifier.weight(1f))
            second.Content(Modifier.weight(1f))
        }
    }
}
