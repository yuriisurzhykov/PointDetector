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
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver

/** An icon on a tinted rounded square, the leading part of an icon row. */
@Immutable
internal class PdIconSquare(private val icon: PdDrawableResolver) : PdBaseComponent() {

    @Composable
    override fun Render(modifier: Modifier) {
        val primary = PdTheme.colors.primary
        Box(
            modifier = modifier
                .size(PdTheme.dimensions.component.rowIconBox)
                .clip(PdTheme.shapes.small)
                .background(primary.container),
            contentAlignment = Alignment.Center,
        ) {
            PdTintedImage(icon, primary.onContainer, PdIconSize.Regular.dp())
        }
    }
}
