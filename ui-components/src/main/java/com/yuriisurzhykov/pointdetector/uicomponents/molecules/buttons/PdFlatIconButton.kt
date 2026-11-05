package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A quiet icon button on a tinted square, for actions inside of a bar. */
@Immutable
class PdFlatIconButton(
    icon: PdDrawableResolver,
    description: PdTextResolver,
    onClick: () -> Unit,
) : PdBaseIconButton(icon, description, onClick) {

    @Composable
    override fun container(): Color = PdTheme.colors.surface.container

    @Composable
    override fun tint(): Color = PdTheme.colors.content.primary
}
