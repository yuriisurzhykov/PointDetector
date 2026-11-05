package com.yuriisurzhykov.pointdetector.uicomponents.atoms.status

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdMapMarker

/** "You are here": a brand dot with a white edge and a soft halo. */
@Immutable
object PdUserDot : PdBaseComponent(), PdMapMarker {

    @Composable
    override fun Render(modifier: Modifier) {
        val component = PdTheme.dimensions.component
        val colors = PdTheme.colors
        Box(modifier.size(component.mapPinHaloSize), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(component.mapPinHaloSize)
                    .background(colors.primary.main.copy(alpha = PdTheme.opacity.halo), PdTheme.shapes.full),
            )
            Box(
                Modifier
                    .size(component.mapUserDotSize)
                    .background(colors.primary.main, PdTheme.shapes.full)
                    .border(component.mapPinStroke, colors.surface.primary, PdTheme.shapes.full),
            )
        }
    }
}
