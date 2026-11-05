package com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.LocalPdContentColor

/**
 * Layout, typography and feedback shared by every button. A subclass answers three
 * questions: which colours ([colors]), what happens on a tap ([activate]) and whether it is
 * [enabled]; a loading or disabled button simply has no tap at all.
 */
@Immutable
abstract class PdBaseButton(
    private val text: PdTextResolver,
    private val icon: PdIcon,
) : PdBaseComponent(), PdButton {

    protected open val enabled: Boolean get() = true

    protected open val leading: PdComponent get() = icon

    @Composable
    internal abstract fun colors(): PdButtonColors

    @Composable
    protected abstract fun Modifier.activate(): Modifier

    @Composable
    final override fun Render(modifier: Modifier) {
        val colors = colors()
        val dimensions = PdTheme.dimensions
        val shape = PdTheme.shapes.medium
        val fade = if (enabled) 1f else PdTheme.opacity.disabled
        CompositionLocalProvider(LocalPdContentColor provides colors.content) {
            Row(
                modifier = modifier
                    .graphicsLayer { alpha = fade }
                    .activate()
                    .heightIn(min = dimensions.component.buttonHeight)
                    .clip(shape)
                    .background(colors.container)
                    .border(dimensions.stroke.thin, colors.border, shape)
                    .padding(horizontal = dimensions.spacing.large),
                horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.small, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leading.Content(Modifier)
                PdBasicText(
                    text = text,
                    style = PdTextRole.LabelLarge.style(),
                    color = colors.content,
                    maxLines = 1,
                )
            }
        }
    }
}
