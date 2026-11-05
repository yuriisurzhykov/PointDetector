package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** A raised icon button with a border, for actions that float over a map. */
@Immutable
class PdOutlinedIconButton(
    icon: PdDrawableResolver,
    description: PdTextResolver,
    onClick: () -> Unit,
) : PdBaseIconButton(icon, description, onClick) {

    override val bordered: Boolean = true
    override val elevated: Boolean = true

    @Composable
    override fun container(): Color = PdTheme.colors.surface.primary

    @Composable
    override fun tint(): Color = PdTheme.colors.content.primary
}
