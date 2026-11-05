package com.yuriisurzhykov.pointdetector.uicomponents.organisms.place

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.toggles.PdFavoriteToggle

/** The route button and the favorite toggle on a fade, pinned to the bottom of the place screen. */
@Immutable
class PdPlaceActionBar(
    private val primary: PdButton,
    private val favorite: PdFavoriteToggle,
) : PdBaseComponent(), PdActionBar {

    @Composable
    override fun Render(modifier: Modifier) {
        val spacing = PdTheme.dimensions.spacing
        val colors = PdTheme.colors
        val shape = PdTheme.shapes.medium
        val fade = Brush.verticalGradient(listOf(Color.Transparent, colors.surface.primary, colors.surface.primary))
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(fade)
                .padding(start = spacing.regular, top = spacing.large, end = spacing.regular, bottom = spacing.large),
            horizontalArrangement = Arrangement.spacedBy(spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            primary.Content(Modifier.weight(1f))
            Box(
                Modifier
                    .size(PdTheme.dimensions.component.buttonHeight)
                    .clip(shape)
                    .background(colors.surface.primary)
                    .border(PdTheme.dimensions.stroke.thin, colors.content.secondary, shape),
                contentAlignment = Alignment.Center,
            ) {
                favorite.Content(Modifier)
            }
        }
    }
}
