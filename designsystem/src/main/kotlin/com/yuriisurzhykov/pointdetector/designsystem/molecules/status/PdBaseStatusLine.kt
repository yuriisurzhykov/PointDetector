package com.yuriisurzhykov.pointdetector.designsystem.molecules.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.atoms.status.PdPlaceStatusMark
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.style
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** Mark and title are shared; a subclass may add a line under them through [Hint]. */
@Immutable
abstract class PdBaseStatusLine(
    private val status: PlaceStatus,
    private val title: PdTextResolver,
) : PdBaseComponent(), PdStatusLine {

    @Composable
    protected open fun ColumnScope.Hint() = Unit

    @Composable
    final override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        Column(modifier, verticalArrangement = Arrangement.spacedBy(spacing.xxSmall)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PdPlaceStatusMark(status).Content(Modifier)
                PdBasicText(title, PdTextRole.TitleMedium.style(), PdTheme.colors.content.primary)
            }
            Hint()
        }
    }
}
