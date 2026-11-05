package com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.color

/** An icon that stands alone, so TalkBack reads [description]. */
@Immutable
class PdLabeledIcon(
    private val drawable: PdDrawableResolver,
    private val description: PdTextResolver,
    private val size: PdIconSize = PdIconSize.Regular,
    private val tone: PdContentTone = PdContentTone.Inherit,
) : PdBaseComponent(), PdIcon {

    @Composable
    override fun Render(modifier: Modifier) {
        val label = description.resolve().toString()
        PdTintedImage(drawable, tone.color(), size.dp(), modifier.semantics { contentDescription = label })
    }
}
