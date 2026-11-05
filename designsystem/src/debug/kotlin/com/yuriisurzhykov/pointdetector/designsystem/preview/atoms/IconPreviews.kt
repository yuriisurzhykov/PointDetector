package com.yuriisurzhykov.pointdetector.designsystem.preview.atoms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdLabeledIcon
import com.yuriisurzhykov.pointdetector.designsystem.atoms.icons.PdNoIcon
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.designsystem.icons.PdIcons
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewLabel
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewRow
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.designsystem.preview.pdText
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone

@PdPreviews
@Composable
private fun PdDecorativeIconPreview() {
    PdPreviewFrame {
        PdPreviewLabel("sizes")
        PdPreviewRow {
            PdIconSize.entries.forEach { PdDecorativeIcon(PdIcons.Route, it).Content(Modifier) }
        }
        PdPreviewLabel("tones")
        PdPreviewRow {
            PdContentTone.entries
                .filter { it != PdContentTone.Inverse }
                .forEach { PdDecorativeIcon(PdIcons.Heart, PdIconSize.Large, it).Content(Modifier) }
        }
    }
}

@PdPreviews
@Composable
private fun PdLabeledIconPreview() {
    PdPreviewFrame {
        PdPreviewLabel("an icon that stands alone and is read by TalkBack")
        PdPreviewRow {
            PdLabeledIcon(PdIcons.Route, pdText("Get directions"), PdIconSize.Large, PdContentTone.Brand)
                .Content(Modifier)
            PdLabeledIcon(PdIcons.Close, pdText("Close"), PdIconSize.Large, PdContentTone.Secondary)
                .Content(Modifier)
        }
    }
}

@PdPreviews
@Composable
private fun PdNoIconPreview() {
    PdPreviewFrame {
        PdPreviewLabel("Null Object: between the two texts there is no icon and no gap of its own")
        PdPreviewRow {
            PdPlainText(pdText("before")).Content(Modifier)
            PdNoIcon.Content(Modifier)
            PdPlainText(pdText("after")).Content(Modifier)
        }
    }
}
