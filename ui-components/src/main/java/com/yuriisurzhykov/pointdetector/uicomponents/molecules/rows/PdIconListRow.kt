package com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.dp
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A row with an icon on a tinted square, e.g. a city or an address suggestion. */
@Immutable
class PdIconListRow(
    private val icon: PdDrawableResolver,
    title: PdTextResolver,
    subtitle: PdComponent = PdNoComponent,
    override val trailing: PdComponent = PdNoComponent,
    selected: Boolean = false,
    onClick: () -> Unit,
) : PdBaseListRow(title, subtitle, selected, onClick) {

    override val leading: PdComponent = PdIconSquare(icon)
}
