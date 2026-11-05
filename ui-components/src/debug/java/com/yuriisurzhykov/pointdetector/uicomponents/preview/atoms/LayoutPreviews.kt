package com.yuriisurzhykov.pointdetector.uicomponents.preview.atoms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.layout.PdHairlineDivider
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

@PdPreviews
@Composable
private fun PdHairlineDividerPreview() {
    PdPreviewFrame {
        PdPlainText(pdText("Above the divider")).Content(Modifier)
        PdHairlineDivider.Content(Modifier)
        PdPlainText(pdText("Below the divider")).Content(Modifier)
    }
}
