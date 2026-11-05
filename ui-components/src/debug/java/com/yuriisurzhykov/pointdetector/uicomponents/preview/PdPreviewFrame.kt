package com.yuriisurzhykov.pointdetector.uicomponents.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone

/** Theme, page background and padding around a previewed component. */
@Composable
internal fun PdPreviewFrame(content: @Composable ColumnScope.() -> Unit) {
    PdTheme {
        val spacing = PdTheme.dimensions.spacing
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PdTheme.colors.surface.primary)
                .padding(spacing.regular),
            verticalArrangement = Arrangement.spacedBy(spacing.regular),
            content = content,
        )
    }
}

/** A row of variants of one component, centered vertically with a regular gap. */
@Composable
internal fun PdPreviewRow(content: @Composable RowScope.() -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(PdTheme.dimensions.spacing.regular),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** A small caption above a group of variants. */
@Composable
internal fun PdPreviewLabel(text: String) {
    PdPlainText(PdLiteralText(text), PdTextRole.LabelSmall, PdContentTone.Tertiary).Content(Modifier)
}

internal fun pdText(value: String): PdTextResolver = PdLiteralText(value)
