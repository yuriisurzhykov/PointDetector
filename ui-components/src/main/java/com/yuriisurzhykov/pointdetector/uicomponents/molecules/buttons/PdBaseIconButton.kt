package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdTintedImage
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.dp
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.pdPressable
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/** The shared look of the icon buttons: a touch target, press feedback and a description for TalkBack. */
@Immutable
abstract class PdBaseIconButton(
    private val icon: PdDrawableResolver,
    private val description: PdTextResolver,
    private val onClick: () -> Unit,
) : PdBaseComponent(), PdIconButton {

    protected open val bordered: Boolean get() = false
    protected open val elevated: Boolean get() = false

    @Composable
    internal abstract fun container(): Color

    @Composable
    internal abstract fun tint(): Color

    @Composable
    final override fun Render(modifier: Modifier) {
        val label = description.resolve().toString()
        val shape = PdTheme.shapes.medium
        val lift = if (elevated) Modifier.shadow(PdTheme.dimensions.elevation.small, shape) else Modifier
        val frame = if (bordered) Modifier.border(PdTheme.dimensions.stroke.thin, PdTheme.colors.divider, shape) else Modifier
        Box(
            modifier = modifier
                .pdPressable(Role.Button, onClick)
                .size(PdTheme.dimensions.component.touchTarget)
                .then(lift)
                .clip(shape)
                .background(container())
                .then(frame)
                .semantics { contentDescription = label },
            contentAlignment = Alignment.Center,
        ) {
            PdTintedImage(icon, tint(), PdIconSize.Regular.dp())
        }
    }
}
