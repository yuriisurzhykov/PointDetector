package com.yuriisurzhykov.pointdetector.uicomponents.molecules.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** The title block of the place screen: the name, the address and one line of distances. */
@Immutable
class PdPlaceHeader(
    private val name: PdTextResolver,
    private val address: PdTextResolver,
    private val meta: PdTextResolver,
) : PdBaseComponent(), PdPlaceInfo {

    @Composable
    override fun Render(modifier: Modifier) {
        val content = PdTheme.colors.content
        Column(modifier, verticalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.xxSmall)) {
            PdBasicText(name, PdTextRole.HeadingMedium.style(), content.primary)
            PdBasicText(address, PdTextRole.BodyMedium.style(), content.primary)
            PdBasicText(meta, PdTextRole.BodySmall.style(), content.secondary)
        }
    }
}
