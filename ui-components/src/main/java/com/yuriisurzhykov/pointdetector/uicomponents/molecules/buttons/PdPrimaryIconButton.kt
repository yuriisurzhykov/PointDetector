package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** The main floating action, e.g. "my location". */
@Immutable
class PdPrimaryIconButton(
    icon: PdDrawableResolver,
    description: PdTextResolver,
    onClick: () -> Unit,
) : PdBaseIconButton(icon, description, onClick) {

    override val elevated: Boolean = true

    @Composable
    override fun container(): Color = PdTheme.colors.primary.main

    @Composable
    override fun tint(): Color = PdTheme.colors.primary.onMain
}
