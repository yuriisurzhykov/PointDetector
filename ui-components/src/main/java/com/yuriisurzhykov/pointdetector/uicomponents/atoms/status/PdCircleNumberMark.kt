package com.yuriisurzhykov.pointdetector.uicomponents.atoms.status

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdBasicText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.style
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdBaseComponent
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

@Immutable
class PdCircleNumberMark(
    private val number: Int,
    private val selected: Boolean = false,
) : PdBaseComponent(), PdNumberMark {

    @Composable
    override fun Render(modifier: Modifier) {
        val colors = PdTheme.colors
        val shape = PdTheme.shapes.full
        val label = remember(number) { PdLiteralText(number.toString()) }
        val fill = if (selected) colors.primary.main else Color.Transparent
        val line = if (selected) colors.primary.main else colors.content.secondary
        val text = if (selected) colors.primary.onMain else colors.content.secondary
        Box(
            modifier = modifier
                .size(PdTheme.dimensions.component.numberMarkSize)
                .clip(shape)
                .background(fill)
                .border(PdTheme.dimensions.stroke.thin, line, shape),
            contentAlignment = Alignment.Center,
        ) {
            PdBasicText(label, PdTextRole.LabelSmall.style(), text)
        }
    }
}
