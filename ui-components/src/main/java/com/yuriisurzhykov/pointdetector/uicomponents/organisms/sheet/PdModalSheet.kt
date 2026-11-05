package com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdFlatIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/**
 * A sheet over a dimmed screen for a short task: a title with a close button, the content and an
 * optional footer. A tap on the dimmed part closes it. It rises on a spring when it appears.
 */
@Immutable
class PdModalSheet(
    private val title: PdTextResolver,
    private val closeDescription: PdTextResolver,
    private val onClose: () -> Unit,
    private val content: PdComponent,
    private val footer: PdComponent = PdNoComponent,
) : PdBaseComponent(), PdBottomSheet {

    @Composable
    override fun Render(modifier: Modifier) {
        val component = PdTheme.dimensions.component
        val spacing = PdTheme.dimensions.spacing
        val colors = PdTheme.colors
        val shape = PdTheme.shapes.sheet
        val motion = PdTheme.motion
        val rise = remember { Animatable(1f) }
        LaunchedEffect(Unit) { rise.animateTo(0f, motion.spatial()) }
        val dismiss = remember { MutableInteractionSource() }

        BoxWithConstraints(modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = (1f - rise.value).coerceIn(0f, 1f) }
                    .background(colors.surface.scrim)
                    .clickable(interactionSource = dismiss, indication = null, onClick = onClose),
            )
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .heightIn(max = maxHeight * component.modalMaxFraction)
                    .graphicsLayer { translationY = rise.value.coerceAtLeast(0f) * size.height }
                    .shadow(PdTheme.dimensions.elevation.large, shape)
                    .clip(shape)
                    .background(colors.surface.primary)
                    .border(PdTheme.dimensions.stroke.thin, colors.divider, shape)
                    .pointerInput(Unit) { detectTapGestures { } }
                    .navigationBarsPadding(),
            ) {
                Box(Modifier.fillMaxWidth().padding(top = spacing.small), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .size(component.sheetHandleWidth, component.sheetHandleHeight)
                            .background(colors.divider, PdTheme.shapes.full),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = spacing.regular, end = spacing.small),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PdBasicText(title, PdTextRole.HeadingSmall.style(), colors.content.primary, Modifier.weight(1f), maxLines = 1)
                    PdFlatIconButton(PdIcons.Close, closeDescription, onClose).Content(Modifier)
                }
                Box(Modifier.weight(1f, fill = false)) { content.Content(Modifier) }
                footer.Content(Modifier)
            }
        }
    }
}
