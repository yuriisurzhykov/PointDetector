package com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver

/** An icon in a tinted circle: permissions, empty lists, errors. */
@Immutable
class PdIconIllustration(
    icon: PdDrawableResolver,
    large: Boolean = true,
) : PdBaseIllustration(icon, large) {

    @Composable
    override fun disc(): Color = PdTheme.colors.primary.container

    @Composable
    override fun glyph(): Color = PdTheme.colors.primary.main
}
