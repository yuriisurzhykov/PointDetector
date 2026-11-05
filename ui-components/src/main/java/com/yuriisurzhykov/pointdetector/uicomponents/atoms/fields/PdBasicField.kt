package com.yuriisurzhykov.pointdetector.uicomponents.atoms.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver

/**
 * The one place that draws an input: a frame that turns blue on focus and red on an error, a
 * hint while the value is empty, and slots before and after the text.
 */
@Composable
internal fun PdBasicField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: PdTextResolver,
    modifier: Modifier = Modifier,
    leading: PdComponent = PdNoComponent,
    trailing: PdComponent = PdNoComponent,
    invalid: Boolean = false,
    transformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var focused by remember { mutableStateOf(false) }
    val colors = PdTheme.colors
    val spacing = PdTheme.dimensions.spacing
    val stroke = PdTheme.dimensions.stroke
    val shape = PdTheme.shapes.medium
    val frameColor = when {
        invalid -> colors.status.error.main
        focused -> colors.primary.main
        else -> colors.divider
    }
    val frameWidth = if (invalid || focused) stroke.thick else stroke.thin
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = PdTheme.dimensions.component.fieldMinHeight)
            .clip(shape)
            .background(colors.surface.primary)
            .border(frameWidth, frameColor, shape)
            .padding(start = spacing.regular),
        horizontalArrangement = Arrangement.spacedBy(spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading.Content(Modifier)
        Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (value.isEmpty()) {
                PdBasicText(hint, PdTextRole.BodyLarge.style(), colors.content.secondary, maxLines = 1)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth().onFocusChanged { focused = it.isFocused },
                singleLine = true,
                textStyle = PdTextRole.BodyLarge.style().copy(color = colors.content.primary),
                cursorBrush = SolidColor(colors.primary.main),
                visualTransformation = transformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
            )
        }
        trailing.Content(Modifier)
    }
}
