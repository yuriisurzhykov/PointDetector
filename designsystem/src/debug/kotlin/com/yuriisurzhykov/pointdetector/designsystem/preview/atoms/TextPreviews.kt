package com.yuriisurzhykov.pointdetector.designsystem.preview.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.designsystem.preview.pdText
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone

@PdPreviews
@Composable
private fun PdPlainTextRolesPreview() {
    PdPreviewFrame {
        PdTextRole.entries.forEach { role ->
            PdPlainText(pdText("${role.name}: Food bank on Main Street"), role).Content(Modifier)
        }
    }
}

@PdPreviews
@Composable
private fun PdPlainTextTonesPreview() {
    PdPreviewFrame {
        PdContentTone.entries.forEach { tone ->
            val background = if (tone == PdContentTone.Inverse) {
                Modifier.background(PdTheme.colors.primary.main).padding(PdTheme.dimensions.spacing.small)
            } else {
                Modifier
            }
            Box(background.fillMaxWidth()) {
                PdPlainText(pdText("Tone ${tone.name}"), PdTextRole.TitleMedium, tone).Content(Modifier)
            }
        }
    }
}

@PdPreviews
@Composable
private fun PdPlainTextLayoutPreview() {
    PdPreviewFrame {
        val long = "A very long address of a place that does not fit into a single line of the card"
        PdPreviewLabel("maxLines = 1 (ellipsis)")
        PdPlainText(pdText(long), maxLines = 1).Content(Modifier)
        PdPreviewLabel("unlimited lines")
        PdPlainText(pdText(long)).Content(Modifier)
        PdPreviewLabel("centered")
        PdPlainText(pdText(long), textAlign = TextAlign.Center).Content(Modifier.fillMaxWidth())
    }
}
