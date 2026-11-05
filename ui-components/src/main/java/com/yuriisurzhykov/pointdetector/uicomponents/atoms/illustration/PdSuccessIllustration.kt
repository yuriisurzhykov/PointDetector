package com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver

/** A solid brand circle for a finished action. */
@Immutable
class PdSuccessIllustration(icon: PdDrawableResolver) : PdBaseIllustration(icon, large = true) {

    @Composable
    override fun disc(): Color = PdTheme.colors.primary.main

    @Composable
    override fun glyph(): Color = PdTheme.colors.primary.onMain
}
