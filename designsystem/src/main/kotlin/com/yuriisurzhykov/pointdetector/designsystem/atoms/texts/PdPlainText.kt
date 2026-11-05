package com.yuriisurzhykov.pointdetector.designsystem.atoms.texts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.yuriisurzhykov.pointdetector.designsystem.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.color

@Immutable
class PdPlainText(
    private val text: PdTextResolver,
    private val role: PdTextRole = PdTextRole.BodyMedium,
    private val tone: PdContentTone = PdContentTone.Inherit,
    private val textAlign: TextAlign = TextAlign.Start,
    private val maxLines: Int = Int.MAX_VALUE,
) : PdBaseComponent(), PdText {

    @Composable
    override fun Render(modifier: Modifier) {
        PdBasicText(
            text = text,
            style = role.style(),
            color = tone.color(),
            modifier = modifier,
            textAlign = textAlign,
            maxLines = maxLines,
        )
    }
}
