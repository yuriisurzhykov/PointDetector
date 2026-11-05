package com.yuriisurzhykov.pointdetector.designsystem.atoms.icons

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.size
import com.yuriisurzhykov.pointdetector.designsystem.resolver.drawable.PdDrawableResolver

/** The one place a drawable resolver is drawn: resolve to a painter, tint it with a theme colour. */
@Composable
internal fun PdTintedImage(
    drawable: PdDrawableResolver,
    tint: Color,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = drawable.resolve(),
        contentDescription = null,
        modifier = modifier.size(size),
        colorFilter = ColorFilter.tint(tint),
    )
}
