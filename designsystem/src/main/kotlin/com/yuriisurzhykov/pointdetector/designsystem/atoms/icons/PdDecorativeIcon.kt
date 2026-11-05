package com.yuriisurzhykov.pointdetector.designsystem.atoms.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.color

/** An icon that carries no meaning of its own: a text always sits next to it. */
@Immutable
class PdDecorativeIcon(
    private val drawable: PdDrawableResolver,
    private val size: PdIconSize = PdIconSize.Regular,
    private val tone: PdContentTone = PdContentTone.Inherit,
) : PdBaseComponent(), PdIcon {

    @Composable
    override fun Render(modifier: Modifier) {
        PdTintedImage(drawable, tone.color(), size.dp(), modifier)
    }
}
