package com.yuriisurzhykov.pointdetector.designsystem.resolver.drawable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.painter.Painter

/**
 * Any graphic. Components never take an `ImageVector` or a `@DrawableRes`: they take a
 * resolver and draw whatever [resolve] returns. The result is always a [Painter].
 */
@Stable
interface PdDrawableResolver {

    @Composable
    fun resolve(): Painter
}
