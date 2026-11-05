package com.yuriisurzhykov.pointdetector.uicomponents.molecules.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.status.PdPlaceStatusMark
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** Mark and title are shared; a subclass may add a line under them through [Hint]. */
@Immutable
abstract class PdBaseStatusLine(
    private val status: PlaceStatus,
    private val title: PdTextResolver,
) : PdBaseComponent(), PdStatusLine {

    @Composable
    protected open fun ColumnScope.Hint() = Unit

    /** A secondary line under the title, aligned with the title text and not with the mark. */
    @Composable
    protected fun ColumnScope.HintText(hint: PdTextResolver) {
        val dimensions = PdTheme.dimensions
        PdBasicText(
            text = hint,
            style = PdTextRole.BodySmall.style(),
            color = PdTheme.colors.content.secondary,
            modifier = Modifier.padding(start = dimensions.component.statusMarkSize + dimensions.spacing.small),
        )
    }

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
