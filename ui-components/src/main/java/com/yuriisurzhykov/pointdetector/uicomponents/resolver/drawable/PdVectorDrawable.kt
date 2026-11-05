package com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter

/**
 * The only implementation: a vector. `ImageVector` is immutable, so this class stays
 * `@Immutable` and the painter is cached by `rememberVectorPainter`. XML drawables are
 * converted to `ImageVector` once instead of going through `painterResource`.
 */
@Immutable
class PdVectorDrawable(private val vector: ImageVector) : PdDrawableResolver {

    @Composable
    override fun resolve(): Painter = rememberVectorPainter(vector)
}
