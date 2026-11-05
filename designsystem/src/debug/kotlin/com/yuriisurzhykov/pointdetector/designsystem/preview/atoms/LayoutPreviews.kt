package com.yuriisurzhykov.pointdetector.designsystem.preview.atoms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.designsystem.atoms.layout.PdHairlineDivider
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.designsystem.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.designsystem.preview.pdText

@PdPreviews
@Composable
private fun PdHairlineDividerPreview() {
    PdPreviewFrame {
        PdPlainText(pdText("Above the divider")).Content(Modifier)
        PdHairlineDivider.Content(Modifier)
        PdPlainText(pdText("Below the divider")).Content(Modifier)
    }
}
