package com.yuriisurzhykov.pointdetector.designsystem.atoms.texts

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdTextResolver

/**
 * The one place in the design system that draws text. Resolves the [PdTextResolver]
 * and keeps spans when the resolver returned an `AnnotatedString`.
 */
@Composable
internal fun PdBasicText(
    text: PdTextResolver,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
) {
    val resolved = text.resolve()
    val textStyle = style.copy(color = color, textAlign = textAlign)
    val overflow = if (maxLines == Int.MAX_VALUE) TextOverflow.Clip else TextOverflow.Ellipsis
    if (resolved is AnnotatedString) {
        BasicText(resolved, modifier, textStyle, overflow = overflow, maxLines = maxLines)
    } else {
        BasicText(resolved.toString(), modifier, textStyle, overflow = overflow, maxLines = maxLines)
    }
}
