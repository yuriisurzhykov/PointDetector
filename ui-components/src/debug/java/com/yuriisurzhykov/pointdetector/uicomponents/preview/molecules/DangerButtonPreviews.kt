package com.yuriisurzhykov.pointdetector.uicomponents.preview.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdDangerButton
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviewFrame
import com.yuriisurzhykov.pointdetector.uicomponents.preview.PdPreviews
import com.yuriisurzhykov.pointdetector.uicomponents.preview.pdText

@PdPreviews
@Composable
private fun PdDangerButtonPreview() {
    PdPreviewFrame {
        var clicks by remember { mutableIntStateOf(0) }
        PdDangerButton(
            text = pdText("Delete ($clicks)"),
            icon = PdDecorativeIcon(PdIcons.Delete, PdIconSize.Regular),
            onClick = { clicks++ },
        ).Content(Modifier)
        PdDangerButton(text = pdText("Without an icon"), onClick = { clicks++ }).Content(Modifier)
    }
}
