package com.yuriisurzhykov.pointdetector.uicomponents.molecules.status

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.model.PlaceStatus
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/** A status with extra data: a second line such as "Next: Thu 07:30-13:00". */
@Immutable
class PdHintedStatusLine(
    status: PlaceStatus,
    title: PdTextResolver,
    private val hint: PdTextResolver,
) : PdBaseStatusLine(status, title) {

    @Composable
    override fun ColumnScope.Hint() {
        val dimensions = PdTheme.dimensions
        PdBasicText(
            text = hint,
            style = PdTextRole.BodySmall.style(),
            color = PdTheme.colors.content.secondary,
            modifier = Modifier.padding(start = dimensions.component.statusMarkSize + dimensions.spacing.small),
        )
    }
}
